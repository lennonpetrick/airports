package com.example.airports.presentation.airportlist

import com.example.airports.di.AppComponent
import com.example.airports.di.Components
import com.example.airports.di.FeatureScope
import com.example.airports.di.qualifiers.IOScheduler
import com.example.airports.di.qualifiers.UIScheduler
import com.example.airports.domain.usecases.GetSchipholReachableAirportsUseCase
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable

@Module
internal class AirportListModule {

    @Provides
    fun providesAirportListViewModel(useCase: GetSchipholReachableAirportsUseCase,
                                     mapper: AirportListViewMapper,
                                     @IOScheduler workScheduler: Scheduler,
                                     @UIScheduler uiScheduler: Scheduler,
                                     compositeDisposable: CompositeDisposable): AirportListViewModel {
        return AirportListViewModel(useCase, mapper, workScheduler, uiScheduler, compositeDisposable)
    }

    @Provides
    internal fun providesCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }

}

@FeatureScope
@Component(dependencies = [AppComponent::class], modules = [AirportListModule::class])
internal interface AirportListComponent {
    fun viewModel(): AirportListViewModel

    companion object {
        fun getViewModel(): AirportListViewModel {
            return DaggerAirportListComponent.builder()
                .appComponent(Components.appComponent())
                .build()
                .viewModel()
        }
    }
}