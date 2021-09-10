package com.example.airports.data.repositories

import com.example.airports.assertLastValue
import com.example.airports.data.datasources.local.SettingsLocalDataSource
import com.example.airports.domain.DistanceUnit
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class SettingsRepositoryTest {

    @Mock
    private lateinit var dataSource: SettingsLocalDataSource

    @InjectMocks
    private lateinit var subject: SettingsRepository

    @ParameterizedTest
    @EnumSource(value = DistanceUnit::class)
    fun `When getting distance unit, then it is returned`(unit: DistanceUnit) {
        whenever(dataSource.getDistanceUnit()).thenReturn(Single.just(unit.name))

        subject.getDistanceUnit()
            .test()
            .assertNoErrors()
            .assertLastValue { it == unit }
    }

    @ParameterizedTest
    @EnumSource(value = DistanceUnit::class)
    fun `When saving distance unit, then it returns the result`(unit: DistanceUnit) {
        val result = true
        whenever(dataSource.saveDistanceUnit(unit.name)).thenReturn(Single.just(result))

        subject.saveDistanceUnit(unit)
            .test()
            .assertNoErrors()
            .assertValue(result)
    }

    @Test
    fun `When saving successfully, then it emits updates`() {
        val firstUnit = DistanceUnit.KM
        val secondUnit = DistanceUnit.MI
        whenever(dataSource.getDistanceUnit()).thenReturn(Single.just(firstUnit.name))
        whenever(dataSource.saveDistanceUnit(secondUnit.name)).thenReturn(Single.just(true))

        val observer = subject.getDistanceUnit().test()

        observer.assertLastValue { it == firstUnit }

        subject.saveDistanceUnit(secondUnit)
            .test()
            .assertNoErrors()

        observer.assertLastValue { it == secondUnit }
    }

    @Test
    fun `When saving fails, then it doesn't updates`() {
        val firstUnit = DistanceUnit.KM
        val secondUnit = DistanceUnit.MI
        whenever(dataSource.getDistanceUnit()).thenReturn(Single.just(firstUnit.name))
        whenever(dataSource.saveDistanceUnit(secondUnit.name)).thenReturn(Single.just(false))

        val observer = subject.getDistanceUnit().test()

        observer.assertLastValue { it == firstUnit }

        subject.saveDistanceUnit(secondUnit)
            .test()
            .assertNoErrors()

        observer.assertLastValue { it == firstUnit }
    }
}