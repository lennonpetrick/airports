package com.example.airports.data.repositories

import com.example.airports.data.datasources.CacheDataSource
import com.example.airports.data.datasources.network.FlightNetworkDataSource
import com.example.airports.data.mappers.FlightMapper
import com.example.airports.domain.models.Flight
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class FlightRepository @Inject constructor(
    private val dataSource: FlightNetworkDataSource,
    private val cacheDataSource: CacheDataSource,
    private val mapper: FlightMapper
) {

    companion object {
        const val KEY_FLIGHTS_CACHE = "flights_cache"
    }

    fun getFlights(): Single<List<Flight>> {
        return cacheDataSource.get<List<Flight>>(KEY_FLIGHTS_CACHE)
            .flatMap {
                if (it.isEmpty()) {
                    getFromNetwork()
                } else {
                    Single.just(it.get()!!)
                }
            }
    }

    private fun getFromNetwork(): Single<List<Flight>> {
        return dataSource.getFlights()
            .flattenAsObservable { it }
            .map(mapper::convert)
            .toList()
            .flatMap { cacheDataSource.cache(KEY_FLIGHTS_CACHE, it).toSingleDefault(it) }
    }

}