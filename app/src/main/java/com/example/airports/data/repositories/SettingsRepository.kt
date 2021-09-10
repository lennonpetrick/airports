package com.example.airports.data.repositories

import com.example.airports.data.datasources.local.SettingsLocalDataSource
import com.example.airports.domain.DistanceUnit
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

internal class SettingsRepository @Inject constructor(private val dataSource: SettingsLocalDataSource) {

    private val subject = PublishSubject.create<DistanceUnit>()

    fun getDistanceUnit(): Observable<DistanceUnit> {
        return dataSource.getDistanceUnit()
            .map { DistanceUnit.valueOf(it) }
            .toObservable()
            .concatWith(subject)
    }

    fun saveDistanceUnit(unit: DistanceUnit): Single<Boolean> {
        return dataSource.saveDistanceUnit(unit.name)
            .doOnSuccess {
                if (it) {
                    subject.onNext(unit)
                }
            }
    }

}