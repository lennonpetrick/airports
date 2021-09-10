package com.example.airports.presentation.settings

import com.example.airports.InstantExecutorExtension
import com.example.airports.assertLastValue
import com.example.airports.data.repositories.SettingsRepository
import com.example.airports.domain.DistanceUnit
import com.example.airports.observeStates
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class, InstantExecutorExtension::class)
internal class SettingsViewModelTest {

    @Mock
    private lateinit var repository: SettingsRepository


    private lateinit var subject: SettingsViewModel

    private val compositeDisposable = CompositeDisposable()
    private val scheduler = Schedulers.trampoline()

    @BeforeEach
    fun setUp() {
        subject = SettingsViewModel(repository, scheduler, scheduler, compositeDisposable)
    }

    @Test
    fun `When getting distance unit as kilometers, then it is returned`() {
        whenever(repository.getDistanceUnit()).thenReturn(Observable.just(DistanceUnit.KM))

        val state = subject.observeStates<DistanceUnitState>()
        subject.getDistanceUnit()

        state.assertLastValue { it is DistanceUnitState.Kilometers }
    }

    @Test
    fun `When getting distance unit as miles, then it is returned`() {
        whenever(repository.getDistanceUnit()).thenReturn(Observable.just(DistanceUnit.MI))

        val state = subject.observeStates<DistanceUnitState>()
        subject.getDistanceUnit()

        state.assertLastValue { it is DistanceUnitState.Miles }
    }

    @Test
    fun `When selecting kilometers, then it is saved`() {
        whenever(repository.saveDistanceUnit(DistanceUnit.KM)).thenReturn(Single.just(true))

        subject.selectDistanceUnit(DistanceUnitState.Kilometers)

        verify(repository).saveDistanceUnit(DistanceUnit.KM)
    }

    @Test
    fun `When selecting miles, then it is saved`() {
        whenever(repository.saveDistanceUnit(DistanceUnit.MI)).thenReturn(Single.just(true))

        subject.selectDistanceUnit(DistanceUnitState.Miles)

        verify(repository).saveDistanceUnit(DistanceUnit.MI)
    }
}