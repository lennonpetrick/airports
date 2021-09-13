package com.example.airports.data.repositories

import com.example.airports.data.datasources.network.AirportNetworkDataSource
import com.example.airports.data.mappers.AirportMapper
import com.example.airports.domain.models.Airport
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AirportRepository @Inject constructor(
    private val dataSource: AirportNetworkDataSource,
    private val mapper: AirportMapper,
) {

    fun getAirports(): Single<List<Airport>> {
        return dataSource.getAirports()
            .flattenAsObservable { it }
            .map(mapper::convert)
            .toList()
    }

}