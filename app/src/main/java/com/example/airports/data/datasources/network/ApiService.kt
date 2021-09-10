package com.example.airports.data.datasources.network

import com.example.airports.data.entities.AirportEntity
import com.example.airports.data.entities.FlightEntity
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

internal interface ApiService {

    @GET("test/airports.json")
    fun getAirports(): Single<List<AirportEntity>>

    @GET("test/flights.json")
    fun getFlights(): Single<List<FlightEntity>>

}