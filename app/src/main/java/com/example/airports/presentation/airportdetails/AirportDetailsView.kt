package com.example.airports.presentation.airportdetails

import com.example.airports.presentation.DisplayText

internal class AirportDetailsView(
    val id: String,
    val latitude: String,
    val longitude: String,
    val name: String,
    val city: String,
    val countryId: String,
    val nearestAirport: NearestAirport?
) {
    class NearestAirport(val name: String, val distance: DisplayText)
}