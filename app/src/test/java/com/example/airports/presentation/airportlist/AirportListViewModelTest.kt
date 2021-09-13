package com.example.airports.presentation.airportlist

import com.example.airports.InstantExecutorExtension
import com.example.airports.assertLastValue
import com.example.airports.assertValueAt
import com.example.airports.domain.DistanceUnit
import com.example.airports.domain.models.Airport
import com.example.airports.domain.usecases.GetSchipholReachableAirportsUseCase
import com.example.airports.observeStates
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class, InstantExecutorExtension::class)
internal class AirportListViewModelTest {

    @Mock
    private lateinit var useCase: GetSchipholReachableAirportsUseCase

    @Mock
    private lateinit var mapper: AirportListViewMapper


    private lateinit var subject: AirportListViewModel

    private val compositeDisposable = CompositeDisposable()
    private val scheduler = Schedulers.trampoline()

    @BeforeEach
    fun setUp() {
        subject = AirportListViewModel(useCase, mapper, scheduler, scheduler, compositeDisposable)
    }

    @ParameterizedTest
    @EnumSource(value = DistanceUnit::class)
    fun `When the ViewModel is created, then a list of AirportListView is fetched`(unit: DistanceUnit) {
        val airport = mock<Airport>()
        val airportListView = mock<AirportListView>()
        val distance = 1.0
        val airportWithDistance = GetSchipholReachableAirportsUseCase.AirportWithDistance(airport, distance, unit)
        whenever(useCase.get()).thenReturn(Observable.just(listOf(airportWithDistance)))
        whenever(mapper.convert(airport, distance, unit)).thenReturn(airportListView)

        val state = subject.observeStates<AirportListState>()
        subject.onCreate()

        state.assertValueAt(0) { it is AirportListState.Loading }
            .assertLastValue {
                it is AirportListState.Loaded
                        && it.airportList == listOf(airportListView)
            }
    }

    @Test
    fun `When fetching the list and it fails, then an error is returned`() {
        whenever(useCase.get()).thenReturn(Observable.error(Throwable()))

        val state = subject.observeStates<AirportListState>()
        subject.onCreate()

        state.assertValueAt(0) { it is AirportListState.Loading }
            .assertLastValue { it is AirportListState.Error }
    }

}