package com.example.airports.data.repositories

import com.example.airports.common.Optional
import com.example.airports.data.datasources.CacheDataSource
import com.example.airports.data.datasources.network.AirportNetworkDataSource
import com.example.airports.data.entities.AirportEntity
import com.example.airports.data.mappers.AirportMapper
import com.example.airports.domain.models.Airport
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class AirportRepositoryTest {

    companion object {
        const val KEY_AIRPORTS_CACHE = "airports_cache"
    }

    @Mock
    private lateinit var dataSource: AirportNetworkDataSource

    @Mock
    private lateinit var cacheDataSource: CacheDataSource

    @Mock
    private lateinit var mapper: AirportMapper

    @Mock
    private lateinit var airportEntity: AirportEntity

    @Mock
    private lateinit var airport: Airport

    @InjectMocks
    private lateinit var subject: AirportRepository

    @Test
    fun `When fetching airports and it is cached, then it is returned from cache`() {
        whenever(cacheDataSource.get<List<Airport>>(KEY_AIRPORTS_CACHE)).thenReturn(Single.just(Optional.of(listOf(airport))))

        subject.getAirports()
            .test()
            .assertNoErrors()
            .assertValue(listOf(airport))

        verify(dataSource, never()).getAirports()
    }

    @Test
    fun `When fetching airports and it is not cached, then it fetches from network and caches it`() {
        whenever(cacheDataSource.get<List<Airport>>(KEY_AIRPORTS_CACHE)).thenReturn(Single.just(Optional.empty()))
        whenever(cacheDataSource.cache(KEY_AIRPORTS_CACHE, listOf(airport))).thenReturn(Completable.complete())
        whenever(dataSource.getAirports()).thenReturn(Single.just(listOf(airportEntity)))
        whenever(mapper.convert(airportEntity)).thenReturn(airport)

        subject.getAirports()
            .test()
            .assertNoErrors()
            .assertValue(listOf(airport))

        verify(cacheDataSource).cache(KEY_AIRPORTS_CACHE, listOf(airport))
    }

    @Test
    fun `When fetching airports from network and it fails, then an error is returned`() {
        val exception = Exception()
        whenever(cacheDataSource.get<List<Airport>>(KEY_AIRPORTS_CACHE)).thenReturn(Single.just(Optional.empty()))
        whenever(dataSource.getAirports()).thenReturn(Single.error(exception))

        subject.getAirports()
            .test()
            .assertNoValues()
            .assertError(exception)
    }

}