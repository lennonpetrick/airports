package com.example.airports.data.mappers

import com.example.airports.data.entities.FlightEntity
import com.example.airports.domain.Flight
import javax.inject.Inject

internal class FlightMapper @Inject constructor() {
    fun convert(entity: FlightEntity): Flight {
        return Flight(entity.airlineId, entity.flightNumber,
            entity.departureAirportId, entity.arrivalAirportId)
    }
}