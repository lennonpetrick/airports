package com.example.airports.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

open class BaseViewModel(
    private val workScheduler: Scheduler,
    private val uiScheduler: Scheduler,
    private val compositeDisposable: CompositeDisposable
) : ViewModel(), ComposeLifecycleObserver {

    private val _state = MutableLiveData<ViewState>()

    @Suppress("UNCHECKED_CAST")
    fun <T : ViewState> state(): LiveData<T> = _state as LiveData<T>

    protected fun postState(viewState: ViewState) {
        _state.value = viewState
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    protected fun Disposable.disposeInOnCleared(): Disposable {
        return this.also { compositeDisposable.add(it) }
    }

    protected fun <T> Single<T>.applySchedulers(): Single<T> {
        return this.subscribeOn(workScheduler)
            .observeOn(uiScheduler)
    }
}

interface ViewState