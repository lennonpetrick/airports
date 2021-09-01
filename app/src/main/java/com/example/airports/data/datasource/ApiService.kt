package com.example.airports.data.datasource

import com.example.airports.data.AirportEntity
import com.example.airports.data.FlightEntity
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

internal interface ApiService {

    @GET("test/airports.json")
    fun getAirports(): Single<List<AirportEntity>>

    @GET("test/flights.json")
    fun getFlights(): Single<List<FlightEntity>>

}