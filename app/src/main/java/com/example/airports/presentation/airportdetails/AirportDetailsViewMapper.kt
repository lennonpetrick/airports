package com.example.airports.presentation.airportdetails

import com.example.airports.domain.models.Airport
import com.example.airports.domain.usecases.GetAirportDetailsUseCase
import com.example.airports.presentation.DistanceMapper
import javax.inject.Inject

internal class AirportDetailsViewMapper @Inject constructor(private val distanceMapper: DistanceMapper) {

    fun convert(airport: Airport, nearestAirport: GetAirportDetailsUseCase.NearestAirport?): AirportDetailsView {
        val latitude = airport.latitude.toString()
        val longitude = airport.longitude.toString()

        val nearestAirportView = nearestAirport?.let {
            val distance = distanceMapper.convert(it.distance, it.unit)
            AirportDetailsView.NearestAirport(it.airport.name, distance)
        }

        return AirportDetailsView(
            airport.id, latitude, longitude,
            airport.name, airport.city, airport.countryId, nearestAirportView
        )
    }

}