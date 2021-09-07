package com.example.airports.presentation.airportlist

import androidx.compose.runtime.Immutable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.airports.domain.usecases.GetSchipholReachableAirportsUseCase
import com.example.airports.presentation.BaseViewModel
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable

internal class AirportListViewModel(
    private val useCase: GetSchipholReachableAirportsUseCase,
    private val mapper: AirportListViewMapper,
    workScheduler: Scheduler,
    uiScheduler: Scheduler,
    compositeDisposable: CompositeDisposable
) : BaseViewModel(workScheduler, uiScheduler, compositeDisposable) {

    private val _state = MutableLiveData<ViewState>()
    val state: LiveData<ViewState> = _state

}

internal sealed class ViewState {
    @Immutable class Error(val errorMessage: String?) : ViewState()
    @Immutable class Loaded(val airportList: List<AirportListView>) : ViewState()
}