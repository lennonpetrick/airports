package com.example.airports.domain.models

internal class Flight(
    val airlineId: String,
    val flightNumber: Int,
    val departureAirportId: String,
    val arrivalAirportId: String
)