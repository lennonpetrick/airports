package com.example.airports.domain.usecases

import com.example.airports.common.DistanceHelper
import com.example.airports.data.repositories.AirportRepository
import com.example.airports.data.repositories.FlightRepository
import com.example.airports.domain.DistanceUnit
import com.example.airports.domain.models.Airport
import com.example.airports.domain.models.Flight
import io.reactivex.rxjava3.core.Single
import java.util.*
import javax.inject.Inject

internal class GetSchipholReachableAirportsUseCase @Inject constructor(
    private val airportRepository: AirportRepository,
    private val flightRepository: FlightRepository,
    private val distanceHelper: DistanceHelper
) {

    companion object {
        const val SCHIPHOL_ID = "AMS"
    }

    fun get(): Single<List<AirportWithDistance>> {
        return flightRepository.getFlights()
            .map(::mapToAirportIds)
            .flatMap { airportIds ->
                airportRepository.getAirports()
                    .map { mapToAirportWithDistance(airportIds, it) }
            }
    }

    private fun mapToAirportWithDistance(airportIds: Set<String>,
                                         airports: List<Airport>): List<AirportWithDistance> {
        val airportsMap = airports.map { it.id to it }.toMap()
        val schiphol = airportsMap[SCHIPHOL_ID]
        val airportWithDistanceList = LinkedList<AirportWithDistance>()

        schiphol?.apply {
            airportIds.forEach {
                airportsMap[it]?.also { airport ->
                    val distance = distanceHelper.calculate(schiphol.latitude, schiphol.longitude,
                        airport.latitude, airport.longitude)
                    airportWithDistanceList.add(AirportWithDistance(airport, distance, DistanceUnit.KM)) //Todo distance unit
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