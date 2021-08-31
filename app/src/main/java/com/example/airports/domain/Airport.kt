package com.example.airports.domain

internal class Airport(
    val id: String,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val city: String,
    val countryId: String
)