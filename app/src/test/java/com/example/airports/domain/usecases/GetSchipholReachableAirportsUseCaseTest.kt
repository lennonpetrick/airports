package com.example.airports.domain.usecases

import com.example.airports.TestFactory.createAirport
import com.example.airports.TestFactory.createFlight
import com.example.airports.assertLastValue
import com.example.airports.common.DistanceHelper
import com.example.airports.data.repositories.AirportRepository
import com.example.airports.data.repositories.FlightRepository
import com.example.airports.data.repositories.SettingsRepository
import com.example.airports.domain.DistanceUnit
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class GetSchipholReachableAirportsUseCaseTest {

    companion object {
        const val SCHIPHOL_ID = "AMS"
    }

    @Mock
    private lateinit var airportRepository: AirportRepository

    @Mock
    private lateinit var flightRepository: FlightRepository

    @Mock
    private lateinit var settingsRepository: SettingsRepository

    @Mock
    private lateinit var distanceHelper: DistanceHelper

    @InjectMocks
    private lateinit var subject: GetSchipholReachableAirportsUseCase

    @Test
    fun `When getting airports that are reachable from Schiphol, then it returns the airports and their distance from Schiphol`() {
        val flight = createFlight(departureAirportId = SCHIPHOL_ID, arrivalAirportId = "AAA")
        val airport = createAirport(airportId = "AAA", latitude = 123.0, longitude = 321.0)
        val schiphol = createAirport(airportId = SCHIPHOL_ID, latitude = 456.0, longitude = 654.0)
        val distance = 111.0
        val unit = mock<DistanceUnit>()

        whenever(flightRepository.getFlights()).thenReturn(Single.just(listOf(flight)))
        whenever(airportRepository.getAirports()).thenReturn(Single.just(listOf(airport, schiphol)))
        whenever(settingsRepository.getDistanceUnit()).thenReturn(Observable.just(unit))
        whenever(distanceHelper.calculate(schiphol.latitude, schiphol.longitude,
            airport.latitude, airport.longitude, unit)).thenReturn(distance)

        val observer = subject.get().test()

        observer.assertNoErrors()
            .assertLastValue { result ->
                result[0].let {
                    it.airport == airport
                        && it.distance == distance
                        && it.unit == unit
                }}
    }

    @Test
    fun `When getting airports that are reachable from Schiphol, then it returns the list sorted by the distance from Schiphol`() {
        val flight1 = createFlight(departureAirportId = SCHIPHOL_ID, arrivalAirportId = "AAA")
        val airport1 = createAirport(airportId = "AAA", latitude = 123.0, longitude = 321.0)
        val distance1 = 111.0

        val flight2 = createFlight(departureAirportId = SCHIPHOL_ID, arrivalAirportId = "BBB")
        val airport2 = createAirport(airportId = "BBB", latitude = 478.0, longitude = 9876.0)
        val distance2 = 222.0

        val schiphol = createAirport(airportId = SCHIPHOL_ID, latitude = 456.0, longitude = 654.0)
        val unit = mock<DistanceUnit>()

        whenever(flightRepository.getFlights()).thenReturn(Single.just(listOf(flight1, flight2)))
        whenever(airportRepository.getAirports()).thenReturn(Single.just(listOf(airport1, airport2, schiphol)))
        whenever(settingsRepository.getDistanceUnit()).thenReturn(Observable.just(unit))

        whenever(distanceHelper.calculate(schiphol.latitude, schiphol.longitude,
            airport1.latitude, airport1.longitude, unit)).thenReturn(distance1)

        whenever(distanceHelper.calculate(schiphol.latitude, schiphol.longitude,
            airport2.latitude, airport2.longitude, unit)).thenReturn(distance2)

        val observer = subject.get().test()

        observer.assertNoErrors()
            .assertLastValue { result ->
                result[0].let { it.airport == airport1 && it.distance == distance1 }
                        && result[1].let { it.airport == airport2 && it.distance == distance2 } }
    }

    @Test
    fun `When there's no flight departing from Schiphol, then it returns an empty list`() {
        val flight = createFlight(departureAirportId = "whatever", arrivalAirportId = "AAA")
        val airport = createAirport(airportId = "AAA", latitude = 123.0, longitude = 321.0)
        val schiphol = createAirport(airportId = SCHIPHOL_ID, latitude = 456.0, longitude = 654.0)
        whenever(flightRepository.getFlights()).thenReturn(Single.just(listOf(flight)))
        whenever(airportRepository.getAirports()).thenReturn(Single.just(listOf(airport, schiphol)))
        whenever(settingsRepository.getDistanceUnit()).thenReturn(Observable.just(mock()))

        val observer = subject.get().test()

        observer.assertNoErrors()
            .assertLastValue { result -> result.isEmpty() }
    }

    @Test
    fun `When no flight is found, then it returns an empty list`() {
        whenever(flightRepository.getFlights()).thenReturn(Single.just(emptyList()))
        whenever(airportRepository.getAirports()).thenReturn(Single.just(listOf(createAirport())))
        whenever(settingsRepository.getDistanceUnit()).thenReturn(Observable.just(mock()))

        val observer = subject.get().test()

        observer.assertNoErrors()
            .assertLastValue { result -> result.isEmpty() }
    }

    @Test
    fun `When no airport is found, then it returns an empty list`() {
        val flight = createFlight(departureAirportId = SCHIPHOL_ID, arrivalAirportId = "AAA")
        whenever(flightRepository.getFlights()).thenReturn(Single.just(listOf(flight)))
        whenever(airportRepository.getAirports()).thenReturn(Single.just(emptyList()))
        whenever(settingsRepository.getDistanceUnit()).thenReturn(Observable.just(mock()))

        val observer = subject.get().test()

        observer.assertNoErrors()
            .assertLastValue { result -> result.isEmpty() }
    }

    @Test
    fun `When getting the flights fails, then it returns an error`() {
        val exception = Throwable()
        whenever(flightRepository.getFlights()).thenReturn(Single.error(exception))
        whenever(airportRepository.getAirports()).thenReturn(Single.just(mock()))
        whenever(settingsRepository.getDistanceUnit()).thenReturn(Observable.just(mock()))

        val observer = subject.get().test()

        observer.assertNoValues()
            .assertError(exception)
    }

    @Test
    fun `When getting the airports fails, then it returns an error`() {
        val exception = Throwable()
        val flight = createFlight(departureAirportId = SCHIPHOL_ID, arrivalAirportId = "AAA")
        whenever(flightRepository.getFlights()).thenReturn(Single.just(listOf(flight)))
        whenever(airportRepository.getAirports()).thenReturn(Single.error(exception))
        whenever(settingsRepository.getDistanceUnit()).thenReturn(Observable.just(mock()))

        val observer = subject.get().test()

        observer.assertNoValues()
            .assertError(exception)
    }
}