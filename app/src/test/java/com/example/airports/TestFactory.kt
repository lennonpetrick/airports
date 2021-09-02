package com.example.airports

import com.example.airports.data.entities.AirportEntity
import com.example.airports.data.entities.FlightEntity
import com.example.airports.domain.models.Airport

internal object TestFactory {

    fun createAirport(airportId: String = "id",
                      latitude: Double = Double.MAX_VALUE,
                      longitude: Double = Double.MAX_VALUE): Airport {
        return Airport(airportId, latitude, longitude,
            "name", "city", "countryId")
    }

    fun createAirportEntity(): AirportEntity {
        return AirportEntity("id", Double.MAX_VALUE, Double.MAX_VALUE,
            "name", "city", "countryId")
    }

    fun createFlightEntity(): FlightEntity {
        return FlightEntity("airlineId", Int.MAX_VALUE,
            "departureAirportId", "arrivalAirportId")
    }

}