package com.example.airports.presentation.airportlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import com.example.airports.di.daggerViewModel

@Composable
internal fun AirportListScreen(
    viewModel: AirportListViewModel = daggerViewModel {
        AirportListComponent.getViewModel()
    }
) {
    val viewState = viewModel.state.observeAsState()
}