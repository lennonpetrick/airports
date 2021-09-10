package com.example.airports.data.datasources.local

import android.content.SharedPreferences
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class SettingsLocalDataSource @Inject constructor(private val preferences: SharedPreferences) {

    companion object {
        const val KEY_DISTANCE_UNIT = "distance_unit"
        const val DEFAULT_DISTANCE_UNIT = "KM"
    }

    fun getDistanceUnit(): Single<String> {
        return Single.fromCallable { preferences.getString(KEY_DISTANCE_UNIT, DEFAULT_DISTANCE_UNIT) }
    }

    fun saveDistanceUnit(unit: String): Single<Boolean> {
        return Single.fromCallable {
            preferences.edit()
                .apply {
                    putString(KEY_DISTANCE_UNIT, unit)
                }.commit()
        }
    }

}