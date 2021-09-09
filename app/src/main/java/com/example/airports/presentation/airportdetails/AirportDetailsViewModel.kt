package com.example.airports.presentation.airportdetails

import androidx.compose.runtime.Immutable
import com.example.airports.domain.usecases.GetAirportDetailsUseCase
import com.example.airports.presentation.BaseViewModel
import com.example.airports.presentation.ViewState
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable

internal class AirportDetailsViewModel(
    private val useCase: GetAirportDetailsUseCase,
    private val mapper: AirportDetailsViewMapper,
    workScheduler: Scheduler,
    uiScheduler: Scheduler,
    compositeDisposable: CompositeDisposable
) : BaseViewModel(workScheduler, uiScheduler, compositeDisposable) {

    fun getAirportDetails(id: String) {
        postState(AirportDetailsState.Loading)
        useCase.get(id)
            .map { result ->
                when (result) {
                    is GetAirportDetailsUseCase.Result.Value -> {
                        val details = mapper.convert(result.airport, result.nearestAirport)
                        AirportDetailsState.Loaded(details)
                    }
                    is GetAirportDetailsUseCase.Result.Empty -> AirportDetailsState.Error
                }
            }
            .applySchedulers()
            .subscribe(::postState) { postState(AirportDetailsState.Error) }
            .disposeInOnCleared()
    }

}

internal sealed class AirportDetailsState: ViewState {
    object Loading : AirportDetailsState()
    object Error : AirportDetailsState()
    @Immutable class Loaded(val airportDetails: AirportDetailsView) : AirportDetailsState()
}