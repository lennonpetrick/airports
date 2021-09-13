package com.example.airports.data.datasources

import com.example.airports.assertLastValue
import com.nhaarman.mockito_kotlin.mock
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class CacheDataSourceTest {

    private lateinit var subject: CacheDataSource

    @BeforeEach
    fun setUp() {
        subject = CacheDataSource()
    }

    @Test
    fun `When an object is cached, then a completable is returned`() {
        subject.cache("key", mock())
            .test()
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun `When requesting the object and it is cached, then returns an optimal with that object`() {
        val key = "key"
        val mock = mock<Any>()
        subject.cache(key, mock).blockingAwait()

        subject.get<Any>(key)
            .test()
            .assertNoErrors()
            .assertLastValue { optional -> optional.get() == mock }
    }

    @Test
    fun `When requesting the object but it is not cached, then returns an optimal empty`() {
        subject.get<Any>("key")
            .test()
            .assertNoErrors()
            .assertLastValue { optional -> optional.isEmpty() }
    }

}