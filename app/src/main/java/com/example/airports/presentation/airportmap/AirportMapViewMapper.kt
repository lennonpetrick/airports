package com.example.airports.presentation.airportmap

import com.example.airports.domain.models.Airport
import javax.inject.Inject

internal class AirportMapViewMapper @Inject constructor() {

    fun convert(airport: Airport, furthestAirports: Pair<Airport, Airport>?): AirportMapView {
        val furthest = furthestAirports?.let {
            it.first.id == airport.id || it.second.id == airport.id
        } ?: false

        return AirportMapView(
            airport.id, furthest, airport.name, airport.latitude,
            airport.longitude, airport.city, airport.countryId
        )
    }

}