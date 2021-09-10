package com.example.airports.presentation.airportdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.airports.R
import com.example.airports.di.daggerViewModel
import com.example.airports.presentation.DisplayText
import com.example.airports.presentation.components.GenericError
import com.example.airports.presentation.components.Loading
import com.example.airports.presentation.components.TopBar

class AirportDetailsActivity : AppCompatActivity() {

    companion object {
        private const val KEY_AIRPORT_ID = "airport_id"

        fun start(context: Context, airportId: String) {
            Intent(context, AirportDetailsActivity::class.java).apply {
                putExtra(KEY_AIRPORT_ID, airportId)
                context.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = intent?.getStringExtra(KEY_AIRPORT_ID)
            ?: throw IllegalStateException("Trying to get airport id from a null Bundle!")

        setContent {
            Scaffold(
                topBar = {
                    TopBar(
                        title = stringResource(R.string.airport_details_title),
                        onBackClick = { onBackPressed() }
                    )
                },
            ) {
                val viewModel = daggerViewModel { AirportDetailsComponent.getViewModel() }
                viewModel.getAirportDetails(id)
                OnStateChanges(viewModel)
            }
        }
    }

    @Composable
    private fun OnStateChanges(viewModel: AirportDetailsViewModel) {
        val state by viewModel.state<AirportDetailsState>().observeAsState()
        state?.apply {
            when (this) {
                is AirportDetailsState.Loading -> Loading()
                is AirportDetailsState.Loaded -> AirportDetails(airportDetails)
                is AirportDetailsState.Error -> GenericError()
            }
        }
    }

    @Composable
    private fun AirportDetails(details: AirportDetailsView) {

    }

    @Preview
    @Composable
    private fun AirportDetailsPreview() {
        val details = AirportDetailsView(
            id = "AMS",
            latitude = "52.30907",
            longitude = "4.763385",
            name = "Amsterdam-Schiphol Airport",
            city = "Amsterdam",
            countryId = "NL",
            nearestAirport = AirportDetailsView.NearestAirport(
                name = "Rotterdam The Hague Airport",
                DisplayText(R.string.distance_km, 42.0)
            )
        )
        AirportDetails(details)
    }
}