package com.example.airports.presentation.airportmap

import com.example.airports.InstantExecutorExtension
import com.example.airports.assertLastValue
import com.example.airports.domain.models.Airport
import com.example.airports.domain.usecases.GetAllAirportsUseCase
import com.example.airports.observeStates
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class, InstantExecutorExtension::class)
internal class AirportMapViewModelTest {

    @Mock
    private lateinit var useCase: GetAllAirportsUseCase

    @Mock
    private lateinit var mapper: AirportMapViewMapper


    private lateinit var subject: AirportMapViewModel

    private val compositeDisposable = CompositeDisposable()
    private val scheduler = Schedulers.trampoline()

    @BeforeEach
    fun setUp() {
        subject = AirportMapViewModel(useCase, mapper, scheduler, scheduler, compositeDisposable)
    }

    @Test
    fun `When the ViewModel is created, then a list of AirportMapView is fetched`() {
        val airport = mock<Airport>()
        val furthestAirports = Pair(mock<Airport>(), mock<Airport>())
        val airportMapView = mock<AirportMapView>()
        val useCaseResult = GetAllAirportsUseCase.Result(listOf(airport), furthestAirports)
        whenever(useCase.get()).thenReturn(Single.just(useCaseResult))
        whenever(mapper.convert(airport, furthestAirports)).thenReturn(airportMapView)

        val state = subject.observeStates<AirportMapState>()
        subject.onCreate()

        state.assertLastValue {
                it is AirportMapState.Loaded
                        && it.airports == listOf(airportMapView)
            }
    }

    @Test
    fun `When fetching the list and it fails, then an error is returned`() {
        whenever(useCase.get()).thenReturn(Single.error(Throwable()))

        val state = subject.observeStates<AirportMapState>()
        subject.onCreate()

        state.assertLastValue { it is AirportMapState.Error }
    }

}