package com.example.airports.common

import javax.inject.Inject
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sqrt

/**
 * This calc is based on Haversine formula (https://en.wikipedia.org/wiki/Haversine_formula) and
 * the code was taken from https://stackoverflow.com/questions/27928/calculate-distance-between-two-latitude-longitude-points-haversine-formula
 * and adapted.
 * */
internal class DistanceHelper @Inject constructor() {

    fun calculate(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val p = Math.PI / 180
        val dLat = lat2 - lat1
        val dLon = lon2 - lon1
        val result = (0.5 - cos(dLat * p)) / 2 +
                cos(lat1 * p) * cos(lat2 * p) *
                (1 - cos(dLon * p)) / 2

        val earthDiameter = 12742 // Earth radius = 6371 km
        return earthDiameter * asin(sqrt(result))
    }

}