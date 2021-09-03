package com.example.airports.presentation

import com.example.airports.di.AppComponent
import com.example.airports.di.Components
import com.example.airports.di.FeatureScope
import dagger.Component

@FeatureScope
@Component(dependencies = [AppComponent::class])
internal interface MainActivityComponent {
    fun inject(activity: MainActivity)

    companion object {
        fun init(activity: MainActivity) {
            DaggerMainActivityComponent.builder()
                .appComponent(Components.appComponent())
                .build()
                .inject(activity)
        }
    }
}