package com.example.airports.di

import com.example.airports.data.repositories.AirportRepository
import com.example.airports.data.repositories.FlightRepository
import com.example.airports.data.repositories.SettingsRepository
import com.example.airports.di.modules.LocalModule
import com.example.airports.di.modules.NetworkModule
import com.example.airports.di.modules.SchedulerModule
import com.example.airports.di.qualifiers.IOScheduler
import com.example.airports.di.qualifiers.UIScheduler
import dagger.Component
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, LocalModule::class, SchedulerModule::class])
internal interface AppComponent {

    @IOScheduler
    fun ioScheduler(): Scheduler

    @UIScheduler
    fun uiScheduler(): Scheduler

    fun flightRepository(): FlightRepository

    fun airportRepository(): AirportRepository

    fun settingsRepository(): SettingsRepository

}