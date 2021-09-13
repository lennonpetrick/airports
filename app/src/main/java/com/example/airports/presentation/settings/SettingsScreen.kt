package com.example.airports.presentation.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.airports.R
import com.example.airports.di.daggerViewModel

@Composable
internal fun SettingsScreen(
    viewModel: SettingsViewModel = daggerViewModel {
        SettingsComponent.getViewModel()
    }
) {
    val state = viewModel.state<DistanceUnitState>().observeAsState(DistanceUnitState.Kilometers)
    viewModel.getDistanceUnit()
    MainContent(state) { viewModel.selectDistanceUnit(it) }
}

@Composable
private fun MainContent(
    selectedUnit: State<DistanceUnitState>,
    onSelectedChange: (DistanceUnitState) -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Text(
            text = stringResource(R.string.settings_subtitle),
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(16.dp)
        )

        ChooseOptions(selected = selectedUnit, onSelectedChange)
    }
}

@Composable
private fun ChooseOptions(
    selected: State<DistanceUnitState>,
    onSelectedChange: (DistanceUnitState) -> Unit
) {
    val unitOptions = UnitRadioOption.getList()

    Column {
        unitOptions.forEach { option ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .selectable(
                        selected = (option.state == selected.value),
                        onClick = { onSelectedChange(option.state) }
                    )
            ) {
                RadioButton(
                    selected = (option.state == selected.value),
                    onClick = { onSelectedChange(option.state) }
                )
                Text(
                    text = stringResource(option.text),
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

private sealed class UnitRadioOption(
    @StringRes val text: Int,
    val state: DistanceUnitState
) {

    companion object {
        fun getList(): List<UnitRadioOption> {
            return listOf(Kilometers, Miles)
        }
    }

    object Kilometers : UnitRadioOption(text = R.string.settings_option_kilometers, state = DistanceUnitState.Kilometers)
    object Miles : UnitRadioOption(text = R.string.settings_option_miles, state = DistanceUnitState.Miles)
}

@Preview
@Composable
private fun MainContentPreview() {
    MainContent(derivedStateOf { DistanceUnitState.Kilometers }) {}
}