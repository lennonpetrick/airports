package com.example.airports.data

internal class FlightEntity(
    val airlineId: String,
    val flightNumber: Int,
    val departureAirportId: String,
    val arrivalAirportId: String
)