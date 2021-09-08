package com.example.airports.presentation.airportmap

import com.example.airports.di.AppComponent
import com.example.airports.di.Components
import com.example.airports.di.FeatureScope
import com.example.airports.di.qualifiers.IOScheduler
import com.example.airports.di.qualifiers.UIScheduler
import com.example.airports.domain.usecases.GetAllAirportsUseCase
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable

@Module
internal class AirportMapModule {

    @Provides
    fun providesAirportMapViewModel(useCase: GetAllAirportsUseCase,
                                    mapper: AirportMapViewMapper,
                                    @IOScheduler workScheduler: Scheduler,
                                    @UIScheduler uiScheduler: Scheduler,
                                    compositeDisposable: CompositeDisposable
    ): AirportMapViewModel {
        return AirportMapViewModel(useCase, mapper, workScheduler, uiScheduler, compositeDisposable)
    }

    @Provides
    internal fun providesCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }

}

@FeatureScope
@Component(dependencies = [AppComponent::class], modules = [AirportMapModule::class])
internal interface AirportMapComponent {
    fun viewModel(): AirportMapViewModel

    companion object {
        fun getViewModel(): AirportMapViewModel {
            return DaggerAirportMapComponent.builder()
                .appComponent(Components.appComponent())
                .build()
                .viewModel()
        }
    }
}