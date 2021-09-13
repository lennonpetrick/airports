package com.example.airports.presentation.airportlist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.airports.R
import com.example.airports.di.daggerViewModel
import com.example.airports.presentation.DisplayText
import com.example.airports.presentation.components.GenericError
import com.example.airports.presentation.components.Loading

@Composable
internal fun AirportListScreen(
    viewModel: AirportListViewModel = daggerViewModel {
        AirportListComponent.getViewModel()
    }
) {
    val state by viewModel.state<AirportListState>().observeAsState()
    state?.apply {
        when (this) {
            is AirportListState.Loading -> Loading()
            is AirportListState.Loaded -> AirportList(airportList)
            is AirportListState.Error -> GenericError()
        }
    }
}

@Composable
internal fun AirportList(items: List<AirportListView>) {
    LazyColumn(Modifier.fillMaxSize()) {
        items(items) {
            AirportListItem(it)
        }
    }
}

@Composable
internal fun AirportListItem(view: AirportListView) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ItemText(
            text = view.name,
            modifier = Modifier.weight(1f),
            overflow = TextOverflow.Ellipsis
        )

        Spacer(Modifier.width(10.dp))

        ItemText(
            text = view.distance.let { stringResource(it.text, it.argValue) }
        )
    }
}

@Composable
internal fun ItemText(
    text: String,
    modifier: Modifier = Modifier,
    overflow: TextOverflow = TextOverflow.Clip
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.subtitle1,
        maxLines = 1,
        overflow = overflow
    )
}

@Preview
@Composable
internal fun AirportListPreview() {
    val list = (1..5).map { createFakeAirportListView(it) }
    AirportList(list)
}

@Preview
@Composable
internal fun AirportListItemPreview() {
    AirportListItem(createFakeAirportListView())
}

private fun createFakeAirportListView(id: Int = 1) = AirportListView(
    id = id.toString(),
    name = "Airport $id",
    distance = DisplayText(R.string.distance_km, id*id)
)