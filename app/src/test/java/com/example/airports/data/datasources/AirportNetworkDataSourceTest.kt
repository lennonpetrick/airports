package com.example.airports.data.datasources

import com.example.airports.data.entities.AirportEntity
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class AirportNetworkDataSourceTest {

    @Mock
    private lateinit var service: ApiService

    @InjectMocks
    private lateinit var subject: AirportNetworkDataSource

    @Test
    fun `When fetching airports, then a list of airports is returned`() {
        val airports = listOf(mock<AirportEntity>())
        whenever(service.getAirports()).thenReturn(Single.just(airports))

        subject.getAirports()
            .test()
            .assertNoErrors()
            .assertValue(airports)
    }

    @Test
    fun `When fetching airports fails, then an error is returned`() {
        val exception = Exception()
        whenever(service.getAirports()).thenReturn(Single.error(exception))

        subject.getAirports()
            .test()
            .assertNoValues()
            .assertError(exception)
    }

}