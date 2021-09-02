package com.example.airports.common

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

internal class DistanceHelperTest {

    private val subject = DistanceHelper()

    @ParameterizedTest
    @ArgumentsSource(LocationsAndDistanceProvider::class)
    fun `When calculates the distance between locations, Then it calculates`(param: LocationsAndDistance) {
        val location1 = param.location1
        val location2 = param.location2

        val distance = subject.calculate(
            location1.first, location1.second,
            location2.first, location2.second
        )

        assertEquals(param.distance, distance)
    }

    internal class LocationsAndDistanceProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                Arguments.of(
                    LocationsAndDistance(
                        location1 = 57.08655 to 9.872241,
                        location2 = 35.049625 to -106.617195,
                        distance = 4269.838770984859
                    )
                ),
                Arguments.of(
                    LocationsAndDistance(
                        location1 = 33.640068 to -84.44403,
                        location2 = 47.433037 to 19.261621,
                        distance = 4362.706510579426
                    )
                ),
                Arguments.of(
                    LocationsAndDistance(
                        location1 = 32.837223 to -115.57472,
                        location2 = 40.904675 to 29.309189,
                        distance = 7826.990893695751
                    )
                ),
            )
        }
    }

    internal class LocationsAndDistance(
        val location1: Pair<Double, Double>,
        val location2: Pair<Double, Double>,
        val distance: Double
    )
}

