package com.example.airports.presentation.airportdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                Modifier
                    .padding(16.dp)
            ) {
                RegularText(text = "${details.id} - ${details.city}, ${details.countryId}")
                RegularSpace()

                RegularText(text = details.name)
                SmallSpace()
                SmallText(text = stringResource(R.string.airport_details_location, details.latitude, details.longitude))

                details.nearestAirport?.apply {
                    LargeSpace()
                    Divider()
                    RegularSpace()

                    LargeText(stringResource(R.string.airport_details_nearest))
                    RegularSpace()

                    RegularText(text = name)
                    SmallSpace()
                    SmallText(text = getDistanceText(distance, details.name))
                }
            }
        }
    }

    @Composable
    private fun LargeSpace() {
        Spacer(modifier = Modifier.height(30.dp))
    }

    @Composable
    private fun RegularSpace() {
        Spacer(modifier = Modifier.height(10.dp))
    }

    @Composable
    private fun SmallSpace() {
        Spacer(modifier = Modifier.height(3.dp))
    }

    @Composable
    private fun LargeText(text: String) {
        Text(
            text = text,
            style = MaterialTheme.typography.h5,
        )
    }

    @Composable
    private fun RegularText(text: String) {
        Text(
            text = text,
            style = MaterialTheme.typography.subtitle1,
        )
    }

    @Composable
    private fun SmallText(text: String) {
        Text(
            text = text,
            style = MaterialTheme.typography.subtitle2,
            color = Color.Gray
        )
    }

    @Composable
    private fun getDistanceText(displayText: DisplayText, fromAirportName: String): String {
        val distanceText = displayText.let { stringResource(it.text, it.argValue) }
        return stringResource(R.string.airport_details_nearest_distance, distanceText, fromAirportName)
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