package com.example.airports.domain.usecases

import com.example.airports.assertLastValue
import com.example.airports.common.DistanceHelper
import com.example.airports.data.repositories.AirportRepository
import com.example.airports.domain.models.Airport
import com.example.airports.domain.usecases.GetAllAirportsUseCase
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class GetAllAirportsUseCaseTest {

    @Mock
    private lateinit var repository: AirportRepository

    @Mock
    private lateinit var distanceHelper: DistanceHelper

    @InjectMocks
    private lateinit var subject: GetAllAirportsUseCase

    @Test
    fun `When getting all airports, then a list of airports is returned`() {
        val airports = listOf(createAirport())
        whenever(repository.getAirports()).thenReturn(Single.just(airports))

        val observer = subject.get().test()

        observer.assertNoErrors()
            .assertLastValue { result -> result.airports == airports }
    }

    @Test
    fun `When getting all airports, then the two furthest airports are returned`() {
        val airport1 = createAirport(latitude = 13423.23, longitude = 543.3245)
        val airport2 = createAirport(latitude = 987.0, longitude = 654.87434)
        val airport3 = createAirport(latitude = 45.09, longitude = 8765.87)
        whenever(repository.getAirports()).thenReturn(Single.just(listOf(airport1, airport2, airport3)))

        whenever(distanceHelper.calculate(airport1.latitude, airport1.longitude,
            airport2.latitude, airport2.longitude)).thenReturn(1.0)

        whenever(distanceHelper.calculate(airport1.latitude, airport1.longitude,
            airport3.latitude, airport3.longitude)).thenReturn(2.0)

        whenever(distanceHelper.calculate(airport2.latitude, airport2.longitude,
            airport3.latitude, airport3.longitude)).thenReturn(3.0)

        val observer = subject.get().test()

        observer.assertNoErrors()
            .assertLastValue { result ->
                result.furthestAirports != null
                        && result.furthestAirports.first == airport2
                        && result.furthestAirports.second == airport3
            }
    }

    @Test
    fun `When airports are an empty list, then there is no furthest airports`() {
        whenever(repository.getAirports()).thenReturn(Single.just(emptyList()))

        val observer = subject.get().test()

        observer.assertNoErrors()
            .assertLastValue { result ->
                result.airports.isEmpty() && result.furthestAirports == null
            }
    }

    @Test
    fun `When there is only one airport in the list, then there is no furthest airports`() {
        val airport = createAirport()
        whenever(repository.getAirports()).thenReturn(Single.just(listOf(airport)))

        val observer = subject.get().test()

        observer.assertNoErrors()
            .assertLastValue { result -> result.furthestAirports == null }
    }

    private fun createAirport(latitude: Double = Double.MAX_VALUE,
                              longitude: Double = Double.MAX_VALUE): Airport {
        return Airport("id", latitude, longitude,
            "name", "city", "countryId")
    }
}