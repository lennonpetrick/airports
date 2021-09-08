package com.example.airports

import com.example.airports.presentation.BaseViewModel
import com.example.airports.presentation.ViewState
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.jupiter.api.Assertions.assertTrue

fun <T> TestObserver<T>.assertLastValue(predicate: (T) -> Boolean): TestObserver<T> {
    if (values().isEmpty()) throw AssertionError("Asserting last value for a stream with no values!")

    assertValueAt(values().size - 1, predicate)
    return this
}

fun <T : ViewState> BaseViewModel.observeStates(): List<T> {
    val states = mutableListOf<T>()
    this.state<T>().observeForever { states.add(it) }
    return states
}

fun <T : ViewState> List<T>.assertValueAt(index: Int, predicate: (T) -> Boolean): List<T> {
    if (index >= size) throw AssertionError("Index out of bounds: $index >= $size")

    assertTrue(predicate(this[index]))
    return this
}

fun <T : ViewState> List<T>.assertLastValue(predicate: (T) -> Boolean): List<T> {
    assertTrue(predicate(this.last()))
    return this
}