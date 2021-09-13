package com.example.airports.data.repositories

import com.example.airports.data.datasources.CacheDataSource
import com.example.airports.data.datasources.network.AirportNetworkDataSource
import com.example.airports.data.mappers.AirportMapper
import com.example.airports.domain.models.Airport
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AirportRepository @Inject constructor(
    private val dataSource: AirportNetworkDataSource,
    private val cacheDataSource: CacheDataSource,
    private val mapper: AirportMapper,
) {

    companion object {
        const val KEY_AIRPORTS_CACHE = "airports_cache"
    }

    fun getAirports(): Single<List<Airport>> {
        return cacheDataSource.get<List<Airport>>(KEY_AIRPORTS_CACHE)
            .flatMap {
                if (it.isEmpty()) {
                    getFromNetwork()
                } else {
                    Single.just(it.get()!!)
                }
            }
    }

    private fun getFromNetwork(): Single<List<Airport>> {
        return dataSource.getAirports()
            .flattenAsObservable { it }
            .map(mapper::convert)
            .toList()
            .flatMap { cacheDataSource.cache(KEY_AIRPORTS_CACHE, it).toSingleDefault(it) }
    }

}