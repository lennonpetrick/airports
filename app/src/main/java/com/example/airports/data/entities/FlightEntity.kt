package com.example.airports.data.entities

internal class FlightEntity(
    val airlineId: String,
    val flightNumber: Int,
    val departureAirportId: String,
    val arrivalAirportId: String
)