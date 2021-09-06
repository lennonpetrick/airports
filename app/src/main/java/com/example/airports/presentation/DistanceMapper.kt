package com.example.airports.presentation

import com.example.airports.R
import com.example.airports.domain.DistanceUnit
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

internal class DistanceMapper @Inject constructor() {

    fun convert(distance: Double, unit: DistanceUnit): DisplayText {
        val stringRes = when (unit) {
            DistanceUnit.KM -> R.string.distance_km
            DistanceUnit.MI -> R.string.distance_mile
        }

        return DisplayText(stringRes, round(distance))
    }

    private fun round(value: Double): Double {
        return BigDecimal.valueOf(value)
            .apply { setScale(2, RoundingMode.HALF_UP) }
            .toDouble()
    }

}