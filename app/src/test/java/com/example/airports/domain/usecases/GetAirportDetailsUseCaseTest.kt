package com.example.airports.domain.usecases

import com.example.airports.TestFactory.createAirport
import com.example.airports.assertLastValue
import com.example.airports.common.DistanceHelper
import com.example.airports.data.repositories.AirportRepository
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class GetAirportDetailsUseCaseTest {

    @Mock
    private lateinit var repository: AirportRepository

    @Mock
    private lateinit var distanceHelper: DistanceHelper

    @InjectMocks
    private lateinit var subject: GetAirportDetailsUseCase

    @Test
    fun `When getting the airport, then it is returned`() {
        val id = "AAA"
        val airports = listOf(createAirport(airportId = id), createAirport(airportId = "BBB"))
        whenever(repository.getAirports()).thenReturn(Single.just(airports))

        val observer = subject.get(id).test()

        observer.assertNoErrors()
            .assertLastValue { result ->
                result is GetAirportDetailsUseCase.Result.Value
                        && result.airport.id == id
            }
    }

    @Test
    fun `When getting the airport, then the nearest airport is returned`() {
        val airport1 = createAirport(airportId = "AAA", latitude = 13423.23, longitude = 543.3245)
        val airport2 = createAirport(airportId = "BBB", latitude = 987.0, longitude = 654.87434)
        val airport3 = createAirport(airportId = "CCC", latitude = 45.09, longitude = 8765.87)
        whenever(repository.getAirports()).thenReturn(Single.just(listOf(airport1, airport2, airport3)))

        whenever(distanceHelper.calculate(airport1.latitude, airport1.longitude,
            airport2.latitude, airport2.longitude)).thenReturn(5.6)

        whenever(distanceHelper.calculate(airport1.latitude, airport1.longitude,
            airport3.latitude, airport3.longitude)).thenReturn(3.4)

        val observer = subject.get("AAA").test()

        observer.assertNoErrors()
            .assertLastValue { result ->
                result is GetAirportDetailsUseCase.Result.Value
                        && result.nearestAirport == airport3
            }
    }

    @Test
    fun `When the airport can't be found, then it returns empty result`() {
        whenever(repository.getAirports()).thenReturn(Single.just(emptyList()))

        val observer = subject.get("AAA").test()

        observer.assertNoErrors()
            .assertLastValue { it is GetAirportDetailsUseCase.Result.Empty }
    }

    @Test
    fun `When there is only one airport, then there is no nearest airport`() {
        val id = "AAA"
        val airport = createAirport(airportId = id)
        whenever(repository.getAirports()).thenReturn(Single.just(listOf(airport)))

        val observer = subject.get(id).test()

        observer.assertNoErrors()
            .assertLastValue { result ->
                result is GetAirportDetailsUseCase.Result.Value
                        && result.nearestAirport == null
            }
    }

    @Test
    fun `When getting airports fails, then it returns an error`() {
        val exception = Throwable()
        whenever(repository.getAirports()).thenReturn(Single.error(exception))

        val observer = subject.get("AAA").test()

        observer.assertNoValues()
            .assertError(exception)
    }

}