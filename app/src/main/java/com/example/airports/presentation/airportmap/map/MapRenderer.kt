package com.example.airports.presentation.airportmap.map

import com.example.airports.presentation.airportmap.AirportMapView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

internal class MapRenderer(val map: GoogleMap) {

    fun addAirportsOnMap(airports: List<AirportMapView>) {
        airports.forEach { map.addMarker(createMarker(it)) }
    }

    private fun createMarker(airport: AirportMapView) = MarkerOptions().apply {
        title(airport.id)
        position(LatLng(airport.latitude, airport.longitude))
        if (airport.furthest) {
            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
        }
    }
}