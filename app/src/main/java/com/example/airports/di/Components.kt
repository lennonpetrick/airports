package com.example.airports.di

import android.content.Context
import com.example.airports.di.modules.LocalModule
import com.example.airports.di.modules.NetworkModule
import com.example.airports.di.modules.SchedulerModule

internal object Components {

    private lateinit var appComponent: AppComponent

    fun init(applicationContext: Context) {
        appComponent = DaggerAppComponent.builder()
            .networkModule(NetworkModule())
            .schedulerModule(SchedulerModule())
            .localModule(LocalModule(applicationContext))
            .build()
    }

    fun appComponent() = appComponent

}