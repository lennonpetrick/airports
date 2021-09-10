package com.example.airports.data.repositories

import com.example.airports.data.datasources.network.FlightNetworkDataSource
import com.example.airports.data.entities.FlightEntity
import com.example.airports.data.mappers.FlightMapper
import com.example.airports.domain.models.Flight
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class FlightRepositoryTest {

    @Mock
    private lateinit var dataSource: FlightNetworkDataSource

    @Mock
    private lateinit var mapper: FlightMapper

    @Mock
    private lateinit var flightEntity: FlightEntity

    @Mock
    private lateinit var flight: Flight

    @InjectMocks
    private lateinit var subject: FlightRepository

    @Test
    fun `When fetching flights, then a list of flights is returned`() {
        whenever(dataSource.getFlights()).thenReturn(Single.just(listOf(flightEntity)))
        whenever(mapper.convert(flightEntity)).thenReturn(flight)

        subject.getFlights()
            .test()
            .assertNoErrors()
            .assertValue(listOf(flight))
    }

    @Test
    fun `When fetching flights fails, then an error is returned`() {
        val exception = Exception()
        whenever(dataSource.getFlights()).thenReturn(Single.error(exception))

        subject.getFlights()
            .test()
            .assertNoValues()
            .assertError(exception)
    }

}