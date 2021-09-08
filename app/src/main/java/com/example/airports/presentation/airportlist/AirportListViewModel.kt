package com.example.airports.presentation.airportlist

import androidx.compose.runtime.Immutable
import com.example.airports.domain.usecases.GetSchipholReachableAirportsUseCase
import com.example.airports.presentation.BaseViewModel
import com.example.airports.presentation.ViewState
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable

internal class AirportListViewModel(
    private val useCase: GetSchipholReachableAirportsUseCase,
    private val mapper: AirportListViewMapper,
    workScheduler: Scheduler,
    uiScheduler: Scheduler,
    compositeDisposable: CompositeDisposable
) : BaseViewModel(workScheduler, uiScheduler, compositeDisposable) {

    override fun onCreate() {
        getAirportList()
    }

    private fun getAirportList() {
        postState(AirportListState.Loading)
        useCase.get()
            .flattenAsObservable { it }
            .map { mapper.convert(it.airport, it.distance, it.unit) }
            .toList()
            .map(AirportListState::Loaded)
            .applySchedulers()
            .subscribe(::postState) { postState(AirportListState.Error) }
            .disposeInOnCleared()
    }

}

internal sealed class AirportListState: ViewState {
    object Loading : AirportListState()
    object Error : AirportListState()
    @Immutable class Loaded(val airportList: List<AirportListView>) : AirportListState()
}