package com.example.airports.data.datasources

import com.example.airports.common.Optional
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class CacheDataSource @Inject constructor() {

    private val cacheMap = mutableMapOf<String, Any>()

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(key: String): Single<Optional<T>> {
        return Single.fromCallable {
            cacheMap[key]?.let { Optional.of(it as T) } ?: Optional.empty()
        }
    }

    fun <T : Any> cache(key: String, value: T): Completable {
        return Completable.fromCallable {
            cacheMap.put(key, value)
        }
    }

}