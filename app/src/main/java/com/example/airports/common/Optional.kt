package com.example.airports.common

class Optional<T> private constructor(private val value: T?) {

    companion object {
        fun <T> of(value: T): Optional<T> {
            return Optional(value)
        }

        fun <T> empty(): Optional<T> {
            return Optional(null)
        }
    }

    fun get(): T? {
        return value
    }

    fun isEmpty(): Boolean {
        return value == null
    }
}