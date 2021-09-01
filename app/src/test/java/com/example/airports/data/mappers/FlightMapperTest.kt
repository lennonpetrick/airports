package com.example.airports.data.mappers

import com.example.airports.data.entities.FlightEntity
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class FlightMapperTest {

    private val subject = FlightMapper()

    @Test
    fun `When converting a flight entity to a model, then it converts properly`() {
        val entity = createEntity()
        val model = subject.convert(entity)

        assertAll("Flight fields", {
            assertEquals(entity.airlineId, model.airlineId)
            assertEquals(entity.flightNumber, model.flightNumber)
            assertEquals(entity.departureAirportId, model.departureAirportId)
            assertEquals(entity.arrivalAirportId, model.arrivalAirportId)
        })
    }

    private fun createEntity(): FlightEntity {
        return FlightEntity("airlineId", Int.MAX_VALUE,
            "departureAirportId", "arrivalAirportId")
    }
}