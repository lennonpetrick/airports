package com.example.airports.data.datasources

import com.example.airports.data.entities.FlightEntity
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class FlightNetworkDataSourceTest {

    @Mock
    private lateinit var service: ApiService

    @InjectMocks
    private lateinit var subject: FlightNetworkDataSource

    @Test
    fun `When fetching flights, then a list of flights is returned`() {
        val flights = listOf(mock<FlightEntity>())
        whenever(service.getFlights()).thenReturn(Single.just(flights))

        subject.getFlights()
            .test()
            .assertNoErrors()
            .assertValue(flights)
    }

    @Test
    fun `When fetching flights fails, then an error is returned`() {
        val exception = Exception()
        whenever(service.getFlights()).thenReturn(Single.error(exception))

        subject.getFlights()
            .test()
            .assertNoValues()
            .assertError(exception)
    }

}