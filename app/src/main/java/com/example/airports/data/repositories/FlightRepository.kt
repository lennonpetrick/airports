package com.example.airports.data.repositories

import com.example.airports.data.datasources.network.FlightNetworkDataSource
import com.example.airports.data.mappers.FlightMapper
import com.example.airports.domain.models.Flight
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class FlightRepository @Inject constructor(
    private val dataSource: FlightNetworkDataSource,
    private val mapper: FlightMapper
) {

    fun getFlights(): Single<List<Flight>> {
        return dataSource.getFlights()
            .flattenAsObservable { it }
            .map(mapper::convert)
            .toList()
    }

}