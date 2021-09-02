package com.example.airports.domain.usecases

import com.example.airports.common.DistanceHelper
import com.example.airports.data.repositories.AirportRepository
import com.example.airports.domain.models.Airport
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class GetAirportDetailsUseCase @Inject constructor(
    private val repository: AirportRepository,
    private val distanceHelper: DistanceHelper
) {

    fun get(id: String): Single<Result> {
        return repository.getAirports()
            .map { list ->
                val airport = list.firstOrNull { it.id == id }
                airport?.let { Result.Value(it, findNearestAirport(it, list)) } ?: Result.Empty
            }
    }

    private fun findNearestAirport(mainAirport: Airport, airports: List<Airport>): Airport? {
        var minDistance = Double.MAX_VALUE
        var nearest: Airport? = null

        airports.forEach {
            if (mainAirport.id != it.id) {
                val distance = distanceHelper.calculate(mainAirport.latitude,
                    mainAirport.longitude, it.latitude, it.longitude)

                if (distance <= minDistance) {
                    minDistance = distance
                    nearest = it
                }
            }
        }

        return nearest
    }

    internal sealed class Result {
        class Value(val airport: Airport, val nearestAirport: Airport?): Result()
        object Empty: Result()
    }
}