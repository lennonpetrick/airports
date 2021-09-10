package com.example.airports.data.datasources.local

import android.content.SharedPreferences
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class SettingsLocalDataSourceTest {

    companion object {
        const val KEY_DISTANCE_UNIT = "distance_unit"
        const val DEFAULT_DISTANCE_UNIT = "KM"
    }

    @Mock
    private lateinit var preferences: SharedPreferences

    @InjectMocks
    private lateinit var subject: SettingsLocalDataSource

    @Test
    fun `When getting distance unit, then it is returned`() {
        val unit = "whatever unit"
        whenever(preferences.getString(KEY_DISTANCE_UNIT, DEFAULT_DISTANCE_UNIT)).thenReturn(unit)

        subject.getDistanceUnit()
            .test()
            .assertNoErrors()
            .assertValue(unit)
    }

    @Test
    fun `When saving distance unit successfully, then it returns true`() {
        val editor = mock<SharedPreferences.Editor>()
        whenever(editor.commit()).thenReturn(true)
        whenever(preferences.edit()).thenReturn(editor)

        subject.saveDistanceUnit("whatever unit")
            .test()
            .assertNoErrors()
            .assertValue(true)
    }

    @Test
    fun `When saving distance unit fails, then it returns false`() {
        val editor = mock<SharedPreferences.Editor>()
        whenever(editor.commit()).thenReturn(false)
        whenever(preferences.edit()).thenReturn(editor)

        subject.saveDistanceUnit("whatever unit")
            .test()
            .assertNoErrors()
            .assertValue(false)
    }

}