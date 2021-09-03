package com.example.airports.di.modules

import com.example.airports.di.qualifiers.IOScheduler
import com.example.airports.di.qualifiers.UIScheduler
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Singleton

@Module
internal class SchedulerModule {

    @Provides
    @Singleton
    @IOScheduler
    internal fun providesIoScheduler(): Scheduler {
        return Schedulers.io()
    }

    @Provides
    @Singleton
    @UIScheduler
    internal fun providesUiScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

}