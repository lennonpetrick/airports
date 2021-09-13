package com.example.airports.domain.usecases

import com.example.airports.common.DistanceHelper
import com.example.airports.data.repositories.AirportRepository
import com.example.airports.data.repositories.SettingsRepository
import com.example.airports.domain.DistanceUnit
import com.example.airports.domain.models.Airport
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

internal class GetAirportDetailsUseCase @Inject constructor(
    private val repository: AirportRepository,
    private val settingsRepository: SettingsRepository,
    private val distanceHelper: DistanceHelper
) {

    fun get(id: String): Observable<Result> {
        return repository.getAirports()
            .toObservable()
            .zipWith(settingsRepository.getDistanceUnit(), { airports, unit ->
                val airport = airports.firstOrNull { it.id == id }
                airport?.let { Result.Value(it, findNearestAirport(it, airports, unit)) } ?: Result.Empty
            })
    }

    private fun findNearestAirport(
        mainAirport: Airport,
        airports: List<Airport>,
        unit: DistanceUnit
    ): NearestAirport? {
        var minDistance = Double.MAX_VALUE
        var nearest: Airport? = null

        airports.forEach {
            if (mainAirport.id != it.id) {
                val distance = distanceHelper.calculate(
                    mainAirport.latitude,
                    mainAirport.longitude, it.latitude, it.longitude, unit
                )

                if (distance <= minDistance) {
                    minDistance = distance
                    nearest = it
                }
            }
        }

        return nearest?.let { NearestAirport(it, minDistance, unit) }
    }

    internal class NearestAirport(val airport: Airport, val distance: Double, val unit: DistanceUnit)

    internal sealed class Result {
        class Value(val airport: Airport, val nearestAirport: NearestAirport?) : Result()
        object Empty : Result()
    }
}