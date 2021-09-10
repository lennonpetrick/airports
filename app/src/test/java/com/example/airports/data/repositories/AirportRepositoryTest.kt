package com.example.airports.data.repositories

import com.example.airports.data.datasources.network.AirportNetworkDataSource
import com.example.airports.data.entities.AirportEntity
import com.example.airports.data.mappers.AirportMapper
import com.example.airports.domain.models.Airport
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class AirportRepositoryTest {

    @Mock
    private lateinit var dataSource: AirportNetworkDataSource

    @Mock
    private lateinit var mapper: AirportMapper

    @Mock
    private lateinit var airportEntity: AirportEntity

    @Mock
    private lateinit var airport: Airport

    @InjectMocks
    private lateinit var subject: AirportRepository

    @Test
    fun `When fetching airports, then a list of airports is returned`() {
        whenever(dataSource.getAirports()).thenReturn(Single.just(listOf(airportEntity)))
        whenever(mapper.convert(airportEntity)).thenReturn(airport)

        subject.getAirports()
            .test()
            .assertNoErrors()
            .assertValue(listOf(airport))
    }

    @Test
    fun `When fetching airports fails, then an error is returned`() {
        val exception = Exception()
        whenever(dataSource.getAirports()).thenReturn(Single.error(exception))

        subject.getAirports()
            .test()
            .assertNoValues()
            .assertError(exception)
    }

}