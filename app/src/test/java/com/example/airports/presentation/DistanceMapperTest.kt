package com.example.airports.presentation

import com.example.airports.R
import com.example.airports.domain.DistanceUnit
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class DistanceMapperTest {

    private val subject = DistanceMapper()

    @Test
    fun `When converting KM distance, then it converts properly`() {
        val unit = DistanceUnit.KM
        val distance = 1.0

        val displayText = subject.convert(distance, unit)

        with(displayText) {
            assertEquals(R.string.distance_km, text)
            assertEquals(distance, argValue)
        }
    }

    @Test
    fun `When converting MI distance, then it converts properly`() {
        val unit = DistanceUnit.MI
        val distance = 1.0

        val displayText = subject.convert(distance, unit)

        with(displayText) {
            assertEquals(R.string.distance_mile, text)
            assertEquals(distance, argValue)
        }
    }

}