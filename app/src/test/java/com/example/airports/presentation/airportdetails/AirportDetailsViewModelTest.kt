package com.example.airports.presentation.airportdetails

import com.example.airports.InstantExecutorExtension
import com.example.airports.assertLastValue
import com.example.airports.assertValueAt
import com.example.airports.domain.models.Airport
import com.example.airports.domain.usecases.GetAirportDetailsUseCase
import com.example.airports.observeStates
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class, InstantExecutorExtension::class)
internal class AirportDetailsViewModelTest {

    @Mock
    private lateinit var useCase: GetAirportDetailsUseCase

    @Mock
    private lateinit var mapper: AirportDetailsViewMapper


    private lateinit var subject: AirportDetailsViewModel

    private val compositeDisposable = CompositeDisposable()
    private val scheduler = Schedulers.trampoline()

    @BeforeEach
    fun setUp() {
        subject = AirportDetailsViewModel(useCase, mapper, scheduler, scheduler, compositeDisposable)
    }

    @Test
    fun `When fetching the airport details, then it is returned`() {
        val id = "AAA"
        val airport = mock<Airport>()
        val nearestAirport = mock<GetAirportDetailsUseCase.NearestAirport>()
        val airportDetailsView = mock<AirportDetailsView>()
        whenever(useCase.get(id)).thenReturn(Observable.just(GetAirportDetailsUseCase.Result.Value(airport, nearestAirport)))
        whenever(mapper.convert(airport, nearestAirport)).thenReturn(airportDetailsView)

        val state = subject.observeStates<AirportDetailsState>()
        subject.getAirportDetails(id)

        state.assertValueAt(0) { it is AirportDetailsState.Loading }
            .assertLastValue {
                it is AirportDetailsState.Loaded
                        && it.airportDetails == airportDetailsView
            }
    }

    @Test
    fun `When no airport details is found, then an error is returned`() {
        val id = "AAA"
        whenever(useCase.get(id)).thenReturn(Observable.just(GetAirportDetailsUseCase.Result.Empty))

        val state = subject.observeStates<AirportDetailsState>()
        subject.getAirportDetails(id)

        state.assertValueAt(0) { it is AirportDetailsState.Loading }
            .assertLastValue { it is AirportDetailsState.Error }
    }

    @Test
    fun `When fetching the details fails, then an error is returned`() {
        val id = "AAA"
        whenever(useCase.get(id)).thenReturn(Observable.error(Throwable()))

        val state = subject.observeStates<AirportDetailsState>()
        subject.getAirportDetails(id)

        state.assertValueAt(0) { it is AirportDetailsState.Loading }
            .assertLastValue { it is AirportDetailsState.Error }
    }
}