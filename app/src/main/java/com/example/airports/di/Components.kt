package com.example.airports.di

import com.example.airports.di.modules.NetworkModule
import com.example.airports.di.modules.SchedulerModule

internal object Components {

    private lateinit var appComponent: AppComponent

    fun init() {
        appComponent = DaggerAppComponent.builder()
            .networkModule(NetworkModule())
            .schedulerModule(SchedulerModule())
            .build()
    }

    fun appComponent() = appComponent

}