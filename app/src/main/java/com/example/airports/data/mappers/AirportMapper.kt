package com.example.airports.data.mappers

import com.example.airports.data.entities.AirportEntity
import com.example.airports.domain.models.Airport
import javax.inject.Inject

internal class AirportMapper @Inject constructor() {
    fun convert(entity: AirportEntity): Airport {
        return Airport(entity.id, entity.latitude, entity.longitude,
            entity.name, entity.city, entity.countryId)
    }
}