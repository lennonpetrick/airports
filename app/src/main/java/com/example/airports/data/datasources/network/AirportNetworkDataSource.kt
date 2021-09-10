package com.example.airports.data.datasources.network

import com.example.airports.data.entities.AirportEntity
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class AirportNetworkDataSource @Inject constructor(private val service: ApiService) {

    fun getAirports(): Single<List<AirportEntity>> {
        return service.getAirports()
    }

}