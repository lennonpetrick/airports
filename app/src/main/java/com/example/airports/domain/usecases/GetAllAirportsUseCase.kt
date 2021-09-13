package com.example.airports.domain.usecases

import com.example.airports.common.DistanceHelper
import com.example.airports.data.repositories.AirportRepository
import com.example.airports.domain.DistanceUnit
import com.example.airports.domain.models.Airport
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class GetAllAirportsUseCase @Inject constructor(
    private val repository: AirportRepository,
    private val distanceHelper: DistanceHelper
) {

    companion object {
        val DEFAULT_UNIT = DistanceUnit.KM
    }

    fun get(): Single<Result> {
        return repository.getAirports()
            .map { Result(it, findFurthestTwoAirports(it)) }
    }

    private fun findFurthestTwoAirports(airports: List<Airport>): Pair<Airport, Airport>? {
        val size = airports.size
        var maxDistance = 0.0
        var furthest1: Airport? = null
        var furthest2: Airport? = null

        airports.forEachIndexed { index, airport1 ->
            for (index2 in index + 1 until size) {

                val airport2 = airports[index2]
                val distance = distanceHelper.calculate(
                    airport1.latitude, airport1.longitude,
                    airport2.latitude, airport2.longitude,
                    DEFAULT_UNIT
                )

                if (distance > maxDistance) {
                    maxDistance = distance
                    furthest1 = airport1
                    furthest2 = airport2
                }
            }
        }

        return if (furthest1 != null && furthest2 != null) furthest1!! to furthest2!! else null
    }

    internal class Result(val airports: List<Airport>, val furthestAirports: Pair<Airport, Airport>?)
}