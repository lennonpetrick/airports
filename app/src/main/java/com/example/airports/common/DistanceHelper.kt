package com.example.airports.common

import javax.inject.Inject
import kotlin.math.*

/**
 * This calc is based on Haversine formula (https://en.wikipedia.org/wiki/Haversine_formula) and
 * the code was taken from https://stackoverflow.com/questions/27928/calculate-distance-between-two-latitude-longitude-points-haversine-formula
 * and adapted.
 * */
internal class DistanceHelper @Inject constructor() {

    fun calculate(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val p = Math.PI / 180
        val dLat = (lat2 - lat1) * p
        val dLon = (lon2 - lon1) * p
        val result = sin(dLat / 2) * sin(dLat / 2) +
                cos(lat1 * p) * cos(lat2 * p) *
                sin(dLon / 2) * sin(dLon / 2)

        val earthDiameter = 12742 // Earth radius = 6371 km
        return earthDiameter * atan2(sqrt(result), sqrt(1 - result))
    }

}