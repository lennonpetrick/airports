package com.example.airports.presentation.airportdetails

import com.example.airports.TestFactory.createAirport
import com.example.airports.domain.DistanceUnit
import com.example.airports.domain.usecases.GetAirportDetailsUseCase
import com.example.airports.presentation.DisplayText
import com.example.airports.presentation.DistanceMapper
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class AirportDetailsViewMapperTest {

    @Mock
    private lateinit var distanceMapper: DistanceMapper

    @InjectMocks
    private lateinit var subject: AirportDetailsViewMapper

    @ParameterizedTest
    @EnumSource(value = DistanceUnit::class)
    fun `When converting airport and nearest airport to AirportDetailsView, then it converts properly`(unit: DistanceUnit) {
        val mainAirport = createAirport()
        val otherAirport = createAirport()
        val nearestAirport = GetAirportDetailsUseCase.NearestAirport(otherAirport, 5.0, unit)
        val distanceDisplayText = mock<DisplayText>()
        whenever(distanceMapper.convert(nearestAirport.distance, nearestAirport.unit)).thenReturn(distanceDisplayText)

        val view = subject.convert(mainAirport, nearestAirport)

        assertAll("AirportDetailsView fields", {
            assertEquals(mainAirport.id, view.id)
            assertEquals(mainAirport.latitude.toString(), view.latitude)
            assertEquals(mainAirport.longitude.toString(), view.longitude)
            assertEquals(mainAirport.name, view.name)
            assertEquals(mainAirport.city, view.city)
            assertEquals(mainAirport.countryId, view.countryId)

            with(view.nearestAirport!!) {
                assertEquals(otherAirport.name, this.name)
                assertEquals(distance, distanceDisplayText)
            }
        })
    }

    @Test
    fun `When there is no nearest airport, then nearest airport is null`() {
        val view = subject.convert(createAirport(), nearestAirport = null)
        assertNull(view.nearestAirport)
    }

}