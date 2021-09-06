package com.example.airports.presentation.airportlist

import com.example.airports.TestFactory.createAirport
import com.example.airports.domain.DistanceUnit
import com.example.airports.presentation.DisplayText
import com.example.airports.presentation.DistanceMapper
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class AirportListViewMapperTest {

    @Mock
    private lateinit var distanceMapper: DistanceMapper

    @InjectMocks
    private lateinit var subject: AirportListViewMapper

    @ParameterizedTest
    @EnumSource(value = DistanceUnit::class)
    fun `When converting airport to AirportListView, then it converts properly`(unit: DistanceUnit) {
        val airport = createAirport()
        val distance = 1.0
        val distanceDisplayText = mock<DisplayText>()
        whenever(distanceMapper.convert(distance, unit)).thenReturn(distanceDisplayText)

        val view = subject.convert(airport, distance, unit)

        assertAll("AirportListView fields", {
            assertEquals(airport.id, view.id)
            assertEquals(airport.name, view.name)
            assertEquals(distanceDisplayText, view.distance)
        })
    }

}