package com.example.airports.presentation.airportmap.map

import android.content.Context
import com.example.airports.presentation.airportmap.AirportMapView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

internal class ClusterRenderer(
    context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<AirportMapView>
) : DefaultClusterRenderer<AirportMapView>(context, map, clusterManager) {

    override fun onBeforeClusterItemRendered(item: AirportMapView, markerOptions: MarkerOptions) {
        if (item.furthest) {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
            markerOptions.zIndex(1f)
        }
    }

    override fun shouldRenderAsCluster(cluster: Cluster<AirportMapView>): Boolean {
        return super.shouldRenderAsCluster(cluster) && cluster.items.none { it.furthest }
    }

}