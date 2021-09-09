package com.example.airports.presentation.airportmap.map

import android.annotation.SuppressLint
import android.content.Context
import com.example.airports.presentation.airportmap.AirportMapView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager

internal class MapRenderer(val map: GoogleMap) {

    @SuppressLint("PotentialBehaviorOverride")
    fun addAirportsOnMap(
        context: Context,
        airports: List<AirportMapView>,
        onMarkerClick: (airport: AirportMapView) -> Unit
    ) {
        val clusterManager = ClusterManager<AirportMapView>(context, map)
        val clusterRenderer = ClusterRenderer(context, map, clusterManager)

        clusterManager.renderer = clusterRenderer

        clusterManager.setOnClusterItemClickListener {
            zoomTo(it.position)
            onMarkerClick(it)
            true
        }

        clusterManager.setOnClusterClickListener {
            clusterZoomIn(it)
            true
        }

        map.setOnMarkerClickListener(clusterManager)
        map.setOnCameraIdleListener(clusterManager)

        clusterManager.addItems(airports)
    }

    private fun clusterZoomIn(cluster: Cluster<AirportMapView>) {
        val builder = LatLngBounds.builder()
        cluster.items.forEach {
            builder.include(it.position)
        }

        map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100))
    }

    private fun zoomTo(latLng: LatLng) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, map.cameraPosition.zoom * 1.70f))
    }
}