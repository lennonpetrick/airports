package com.example.airports.data.datasources.network

import com.example.airports.data.entities.FlightEntity
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class FlightNetworkDataSource @Inject constructor(private val service: ApiService) {

    fun getFlights(): Single<List<FlightEntity>> {
        return service.getFlights()
    }

}