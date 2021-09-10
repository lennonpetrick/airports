package com.example.airports

import android.app.Application
import com.example.airports.di.Components

internal class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Components.init(this)
    }

}