package com.example.airports.presentation.settings

import com.example.airports.data.repositories.SettingsRepository
import com.example.airports.domain.DistanceUnit
import com.example.airports.presentation.BaseViewModel
import com.example.airports.presentation.ViewState
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy

internal class SettingsViewModel(
    private val repository: SettingsRepository,
    workScheduler: Scheduler,
    uiScheduler: Scheduler,
    compositeDisposable: CompositeDisposable
) : BaseViewModel(workScheduler, uiScheduler, compositeDisposable) {

    fun getDistanceUnit() {
        repository.getDistanceUnit()
            .map {
                when (it) {
                    null,
                    DistanceUnit.KM -> DistanceUnitState.Kilometers
                    DistanceUnit.MI -> DistanceUnitState.Miles
                }
            }
            .applySchedulers()
            .subscribeBy(onNext = { postState(it) })
            .disposeInOnCleared()
    }

    fun selectDistanceUnit(selected: DistanceUnitState) {
        val unit = when (selected) {
            DistanceUnitState.Kilometers -> DistanceUnit.KM
            DistanceUnitState.Miles -> DistanceUnit.MI
        }

        repository.saveDistanceUnit(unit)
            .applySchedulers()
            .subscribe()
            .disposeInOnCleared()
    }

}

internal sealed class DistanceUnitState: ViewState {
    object Kilometers : DistanceUnitState()
    object Miles : DistanceUnitState()
}