package com.example.airports.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.airports.di.qualifiers.IOScheduler
import com.example.airports.di.qualifiers.UIScheduler
import com.example.airports.domain.usecases.GetAllAirportsUseCase
import com.example.airports.domain.usecases.GetSchipholReachableAirportsUseCase
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}