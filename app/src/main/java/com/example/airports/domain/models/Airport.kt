package com.example.airports.domain.models

internal class Airport(
    val id: String,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val city: String,
    val countryId: String
)