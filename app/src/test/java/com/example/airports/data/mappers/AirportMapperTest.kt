package com.example.airports.data.mappers

import com.example.airports.data.AirportEntity
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class AirportMapperTest {

    private val subject = AirportMapper()

    @Test
    fun `When converting an airport entity to a model, then it converts properly`() {
        val entity = createEntity()
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

    private fun createEntity(): AirportEntity {
        return AirportEntity("id", Double.MAX_VALUE, Double.MAX_VALUE,
            "name", "city", "countryId")
    }
}