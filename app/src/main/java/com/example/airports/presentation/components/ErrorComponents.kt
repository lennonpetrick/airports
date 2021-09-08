package com.example.airports.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.airports.R

@Preview
@Composable
internal fun GenericErrorPreview() {
    GenericError()
}

@Composable
internal fun GenericError() {
    ErrorMessage(stringResource(R.string.generic_error))
}

@Composable
internal fun ErrorMessage(message: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 22.sp
        )
    }
}