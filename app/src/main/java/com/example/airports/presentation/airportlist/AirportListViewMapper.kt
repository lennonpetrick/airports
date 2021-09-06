package com.example.airports.presentation.airportlist

import com.example.airports.domain.DistanceUnit
import com.example.airports.domain.models.Airport
import com.example.airports.presentation.DistanceMapper
import javax.inject.Inject

internal class AirportListViewMapper @Inject constructor(private val distanceMapper: DistanceMapper) {

    fun convert(airport: Airport, distance: Double, unit: DistanceUnit): AirportListView {
        val distanceDisplayText = distanceMapper.convert(distance, unit)
        return AirportListView(airport.id, airport.name, distanceDisplayText)
    }
}