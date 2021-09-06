package com.example.airports.presentation.airportmap

import com.example.airports.TestFactory.createAirport
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class AirportMapViewMapperTest {

    private val subject = AirportMapViewMapper()

    @Test
    fun `When converting airport and there is any furthest, then it converts properly`() {
        val airport = createAirport("AAA")

        val view = subject.convert(airport, null)

        assertAll("AirportMapView fields", {
            assertEquals(airport.id, view.id)
            assertEquals(airport.latitude, view.latitude)
            assertEquals(airport.longitude, view.longitude)
            assertFalse(view.furthest)
        })
    }

    @Test
    fun `When airport is NOT the furthest, then furthest is false`() {
        val airport = createAirport("AAA")
        val view = subject.convert(airport, Pair(createAirport("BBB"), createAirport("CCC")))
        assertFalse(view.furthest)
    }

    @Test
    fun `When airport IS one of the furthest, then furthest is true`() {
        val airport = createAirport("AAA")
        val view = subject.convert(airport, Pair(airport, createAirport("CCC")))
        assertTrue(view.furthest)
    }

}