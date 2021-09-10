package com.example.airports.presentation.settings

import com.example.airports.data.repositories.SettingsRepository
import com.example.airports.di.AppComponent
import com.example.airports.di.Components
import com.example.airports.di.FeatureScope
import com.example.airports.di.qualifiers.IOScheduler
import com.example.airports.di.qualifiers.UIScheduler
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable

@Module
internal class SettingsModule {

    @Provides
    fun providesSettingsViewModel(
        repository: SettingsRepository,
        @IOScheduler workScheduler: Scheduler,
        @UIScheduler uiScheduler: Scheduler,
        compositeDisposable: CompositeDisposable
    ): SettingsViewModel {
        return SettingsViewModel(repository, workScheduler, uiScheduler, compositeDisposable)
    }

    @Provides
    internal fun providesCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }

}

@FeatureScope
@Component(dependencies = [AppComponent::class], modules = [SettingsModule::class])
internal interface SettingsComponent {
    fun viewModel(): SettingsViewModel

    companion object {
        fun getViewModel(): SettingsViewModel {
            return DaggerSettingsComponent.builder()
                .appComponent(Components.appComponent())
                .build()
                .viewModel()
        }
    }
}