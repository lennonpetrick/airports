package com.example.airports.data.repositories

import com.example.airports.common.Optional
import com.example.airports.data.datasources.CacheDataSource
import com.example.airports.data.datasources.network.FlightNetworkDataSource
import com.example.airports.data.entities.FlightEntity
import com.example.airports.data.mappers.FlightMapper
import com.example.airports.domain.models.Flight
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
internal class FlightRepositoryTest {

    companion object {
        const val KEY_FLIGHTS_CACHE = "flights_cache"
    }

    @Mock
    private lateinit var dataSource: FlightNetworkDataSource

    @Mock
    private lateinit var cacheDataSource: CacheDataSource

    @Mock
    private lateinit var mapper: FlightMapper

    @Mock
    private lateinit var flightEntity: FlightEntity

    @Mock
    private lateinit var flight: Flight

    @InjectMocks
    private lateinit var subject: FlightRepository

    @Test
    fun `When fetching flights and it is cached, then it is returned from cache`() {
        whenever(cacheDataSource.get<List<Flight>>(KEY_FLIGHTS_CACHE)).thenReturn(Single.just(Optional.of(listOf(flight))))

        subject.getFlights()
            .test()
            .assertNoErrors()
            .assertValue(listOf(flight))

        verify(dataSource, never()).getFlights()
    }

    @Test
    fun `When fetching flights and it is not cached, then it fetches from network and caches it`() {
        whenever(cacheDataSource.get<List<Flight>>(KEY_FLIGHTS_CACHE)).thenReturn(Single.just(Optional.empty()))
        whenever(cacheDataSource.cache(KEY_FLIGHTS_CACHE, listOf(flight))).thenReturn(Completable.complete())
        whenever(dataSource.getFlights()).thenReturn(Single.just(listOf(flightEntity)))
        whenever(mapper.convert(flightEntity)).thenReturn(flight)

        subject.getFlights()
            .test()
            .assertNoErrors()
            .assertValue(listOf(flight))

        verify(cacheDataSource).cache(KEY_FLIGHTS_CACHE, listOf(flight))
    }

    @Test
    fun `When fetching flights from network and it fails, then an error is returned`() {
        val exception = Exception()
        whenever(cacheDataSource.get<List<Flight>>(KEY_FLIGHTS_CACHE)).thenReturn(Single.just(Optional.empty()))
        whenever(dataSource.getFlights()).thenReturn(Single.error(exception))

        subject.getFlights()
            .test()
            .assertNoValues()
            .assertError(exception)
    }

}