package com.example.airports.domain.usecases

import com.example.airports.common.DistanceHelper
import com.example.airports.data.repositories.AirportRepository
import com.example.airports.data.repositories.FlightRepository
import com.example.airports.data.repositories.SettingsRepository
import com.example.airports.domain.DistanceUnit
import com.example.airports.domain.models.Airport
import com.example.airports.domain.models.Flight
import io.reactivex.rxjava3.core.Observable
import java.util.*
import javax.inject.Inject

internal class GetSchipholReachableAirportsUseCase @Inject constructor(
    private val airportRepository: AirportRepository,
    private val flightRepository: FlightRepository,
    private val settingsRepository: SettingsRepository,
    private val distanceHelper: DistanceHelper
) {

    companion object {
        const val SCHIPHOL_ID = "AMS"
    }

    fun get(): Observable<List<AirportWithDistance>> {
        return Observable.combineLatest(
            flightRepository.getFlights().map(::mapToAirportIds).toObservable(),
            airportRepository.getAirports().toObservable(),
            settingsRepository.getDistanceUnit(),
            ::mapToAirportWithDistance
        )
    }

    private fun mapToAirportWithDistance(airportIds: Set<String>,
                                         airports: List<Airport>,
                                         unit: DistanceUnit): List<AirportWithDistance> {
        val airportsMap = airports.map { it.id to it }.toMap()
        val schiphol = airportsMap[SCHIPHOL_ID]
        val airportWithDistanceList = LinkedList<AirportWithDistance>()

        schiphol?.apply {
            airportIds.forEach {
                airportsMap[it]?.also { airport ->
                    val distance = distanceHelper.calculate(schiphol.latitude, schiphol.longitude,
                        airport.latitude, airport.longitude, unit)
                    airportWithDistanceList.add(AirportWithDistance(airport, distance, unit))
                }
            }
        }

        airportWithDistanceList.sortBy { it.distance }
        return airportWithDistanceList
    }

    private fun mapToAirportIds(flights: List<Flight>): Set<String> {
        val airportIdsSet = hashSetOf<String>()
        flights.forEach {
            if (it.departureAirportId == SCHIPHOL_ID) {
                airportIdsSet.add(it.arrivalAirportId)
            }
        }
        return airportIdsSet
    }

    internal class AirportWithDistance(val airport: Airport, val distance: Double, val unit: DistanceUnit)
}