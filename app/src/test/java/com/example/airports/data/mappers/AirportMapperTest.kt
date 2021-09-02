package com.example.airports.data.mappers

import com.example.airports.TestFactory.createAirportEntity
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class AirportMapperTest {

    private val subject = AirportMapper()

    @Test
    fun `When converting an airport entity to a model, then it converts properly`() {
        val entity = createAirportEntity()
        val model = subject.convert(entity)

        assertAll("Airport fields", {
            assertEquals(entity.id, model.id)
            assertEquals(entity.latitude, model.latitude)
            assertEquals(entity.longitude, model.longitude)
            assertEquals(entity.name, model.name)
            assertEquals(entity.city, model.city)
            assertEquals(entity.countryId, model.countryId)
        })
    }

}