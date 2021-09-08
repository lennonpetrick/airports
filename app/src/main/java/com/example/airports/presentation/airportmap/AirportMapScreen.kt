package com.example.airports.presentation.airportmap

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.airports.R
import com.example.airports.di.daggerViewModel
import com.example.airports.presentation.airportmap.map.MapRenderer
import com.example.airports.presentation.airportmap.map.rememberMapViewWithLifecycle

@Composable
internal fun AirportMapScreen(
    viewModel: AirportMapViewModel = daggerViewModel {
        AirportMapComponent.getViewModel()
    },
    context: Context = LocalContext.current
) {
    val state by viewModel.state<AirportMapState>().observeAsState()

    MapView { renderer ->
        state?.apply {
            when (this) {
                is AirportMapState.Loaded -> renderer.addAirportsOnMap(airports)
                is AirportMapState.Error -> showGenericError(context)
            }
        }
    }
}

@Composable
private fun MapView(onMapReady: (MapRenderer) -> Unit) {
    val mapView = rememberMapViewWithLifecycle()
    AndroidView(
        factory = { mapView },
        modifier = Modifier.fillMaxSize()
    ) { view -> view.getMapAsync { onMapReady(MapRenderer(it)) } }
}

private fun showGenericError(context: Context) {
    Toast.makeText(context, R.string.generic_error, Toast.LENGTH_LONG).show()
}