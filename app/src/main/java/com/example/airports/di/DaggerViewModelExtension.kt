package com.example.airports.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.airports.presentation.ComposeLifecycleObserver

@Composable
inline fun <reified T : ViewModel> daggerViewModel(
    crossinline viewModelCreator: () -> T
): T = viewModel(
    modelClass = T::class.java,
    factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return viewModelCreator().apply {
                if (this is ComposeLifecycleObserver) {
                    this.onCreate()
                }
            } as T
        }
    }
)