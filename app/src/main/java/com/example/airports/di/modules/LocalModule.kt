package com.example.airports.di.modules

import android.content.Context
import android.content.SharedPreferences
import com.example.airports.BuildConfig
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocalModule(private val applicationContext: Context) {

    @Provides
    @Singleton
    internal fun providesSharedPreferences(): SharedPreferences {
        return applicationContext.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
    }

}