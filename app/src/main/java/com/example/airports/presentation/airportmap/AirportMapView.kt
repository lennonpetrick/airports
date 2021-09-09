package com.example.airports.presentation.airportmap

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

internal class AirportMapView(
    val id: String,
    val furthest: Boolean,
    name: String,
    latitude: Double,
    longitude: Double,
    city: String,
    countryId: String
) : ClusterItem {

    private val position = LatLng(latitude, longitude)
    private val title = name
    private val snippet = "$id, $city - $countryId"

    override fun getPosition(): LatLng {
        return position
    }

    override fun getTitle(): String {
        return title
    }

    override fun getSnippet(): String {
        return snippet
    }
}