package com.example.airports.presentation.airportmap

import androidx.compose.runtime.Immutable
import com.example.airports.domain.usecases.GetAllAirportsUseCase
import com.example.airports.presentation.BaseViewModel
import com.example.airports.presentation.ViewState
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable

internal class AirportMapViewModel(
    private val useCase: GetAllAirportsUseCase,
    private val mapper: AirportMapViewMapper,
    workScheduler: Scheduler,
    uiScheduler: Scheduler,
    compositeDisposable: CompositeDisposable
) : BaseViewModel(workScheduler, uiScheduler, compositeDisposable) {

    override fun onCreate() {
        getAllAirports()
    }

    private fun getAllAirports() {
        useCase.get()
            .map { result ->
                val list = result.airports.map {
                    mapper.convert(it, result.furthestAirports)
                }

                AirportMapState.Loaded(list)
            }
            .applySchedulers()
            .subscribe(::postState) { postState(AirportMapState.Error) }
            .disposeInOnCleared()
    }
}

internal sealed class AirportMapState: ViewState {
    object Error : AirportMapState()
    @Immutable class Loaded(val airports: List<AirportMapView>) : AirportMapState()
}