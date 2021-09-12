package com.example.airports.common

import com.example.airports.domain.DistanceUnit
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
            location2.first, location2.second,
            unit = param.unit
        )

        assertEquals(param.distance, distance)
    }

    internal class LocationsAndDistanceProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
            return Stream.of(
                Arguments.of(
                    LocationsAndDistance(
                        location1 = 45.733242 to 16.06152,
                        location2 = 52.30907 to 4.763385,
                        distance = 1099.2832728616806,
                        unit = DistanceUnit.KM
                    )
                ),
                Arguments.of(
                    LocationsAndDistance(
                        location1 = 25.248665 to 55.352917,
                        location2 = 52.30907 to 4.763385,
                        distance = 5168.280571167744,
                        unit = DistanceUnit.KM
                    )
                ),
                Arguments.of(
                    LocationsAndDistance(
                        location1 = 57.08655 to 9.872241,
                        location2 = 35.049625 to -106.617195,
                        distance = 8174.9357599374625,
                        unit = DistanceUnit.KM
                    )
                ),
                Arguments.of(
                    LocationsAndDistance(
                        location1 = 33.640068 to -84.44403,
                        location2 = 47.433037 to 19.261621,
                        distance = 8235.558011944086,
                        unit = DistanceUnit.KM
                    )
                ),
                Arguments.of(
                    LocationsAndDistance(
                        location1 = 32.837223 to -115.57472,
                        location2 = 40.904675 to 29.309189,
                        distance = 11059.589826737658,
                        unit = DistanceUnit.KM
                    )
                ),
                Arguments.of(
                    LocationsAndDistance(
                        location1 = 45.733242 to 16.06152,
                        location2 = 52.30907 to 4.763385,
                        distance = 683.0705730034251,
                        unit = DistanceUnit.MI
                    )
                ),
                Arguments.of(
                    LocationsAndDistance(
                        location1 = 25.248665 to 55.352917,
                        location2 = 52.30907 to 4.763385,
                        distance = 3211.4564629004653,
                        unit = DistanceUnit.MI
                    )
                ),
                Arguments.of(
                    LocationsAndDistance(
                        location1 = 57.08655 to 9.872241,
                        location2 = 35.049625 to -106.617195,
                        distance = 5079.726210397179,
                        unit = DistanceUnit.MI
                    )
                ),
                Arguments.of(
                    LocationsAndDistance(
                        location1 = 33.640068 to -84.44403,
                        location2 = 47.433037 to 19.261621,
                        distance = 5117.395551355242,
                        unit = DistanceUnit.MI
                    )
                ),
                Arguments.of(
                    LocationsAndDistance(
                        location1 = 32.837223 to -115.57472,
                        location2 = 40.904675 to 29.309189,
                        distance = 6872.1871301348365,
                        unit = DistanceUnit.MI
                    )
                ),
            )
        }
    }

    internal class LocationsAndDistance(
        val location1: Pair<Double, Double>,
        val location2: Pair<Double, Double>,
        val distance: Double,
        val unit: DistanceUnit
    )
}

