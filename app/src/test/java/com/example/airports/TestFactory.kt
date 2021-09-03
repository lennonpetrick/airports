package com.example.airports

import com.example.airports.data.entities.AirportEntity
import com.example.airports.data.entities.FlightEntity
import com.example.airports.domain.models.Airport
import com.example.airports.domain.models.Flight

internal object TestFactory {

    fun createAirport(airportId: String = "id",
                      latitude: Double = Double.MAX_VALUE,
                      longitude: Double = Double.MAX_VALUE): Airport {
        return Airport(airportId, latitude, longitude,
            "name", "city", "countryId")
    }

    fun createAirportEntity(): AirportEntity {
        return AirportEntity("id", latitude = Double.MAX_VALUE, longitude = Double.MAX_VALUE,
            "name", "city", "countryId")
    }

    fun createFlight(departureAirportId: String = "departureAirportId",
                     arrivalAirportId: String = "arrivalAirportId"): Flight {
        return Flight("airlineId", flightNumber =  Int.MAX_VALUE,
            departureAirportId, arrivalAirportId)
    }

    fun createFlightEntity(): FlightEntity {
        return FlightEntity("airlineId", flightNumber =  Int.MAX_VALUE,
            "departureAirportId", "arrivalAirportId")
    }

}