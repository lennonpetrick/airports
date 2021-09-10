package com.example.airports.presentation.airportdetails

import com.example.airports.di.AppComponent
import com.example.airports.di.Components
import com.example.airports.di.FeatureScope
import com.example.airports.di.qualifiers.IOScheduler
import com.example.airports.di.qualifiers.UIScheduler
import com.example.airports.domain.usecases.GetAirportDetailsUseCase
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable

@Module
internal class AirportDetailsModule {

    @Provides
    fun providesAirportDetailsViewModel(
        useCase: GetAirportDetailsUseCase,
        mapper: AirportDetailsViewMapper,
        @IOScheduler workScheduler: Scheduler,
        @UIScheduler uiScheduler: Scheduler,
        compositeDisposable: CompositeDisposable
    ): AirportDetailsViewModel {
        return AirportDetailsViewModel(useCase, mapper, workScheduler, uiScheduler, compositeDisposable)
    }

    @Provides
    internal fun providesCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }

}

@FeatureScope
@Component(dependencies = [AppComponent::class], modules = [AirportDetailsModule::class])
internal interface AirportDetailsComponent {
    fun viewModel(): AirportDetailsViewModel

    companion object {
        fun getViewModel(): AirportDetailsViewModel {
            return DaggerAirportDetailsComponent.builder()
                .appComponent(Components.appComponent())
                .build()
                .viewModel()
        }
    }
}