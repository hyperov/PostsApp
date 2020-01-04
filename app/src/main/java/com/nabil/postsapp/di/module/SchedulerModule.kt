package com.nabil.postsapp.di.module

import com.nabil.postsapp.scheduler.BaseSchedulerProvider
import com.nabil.postsapp.scheduler.SchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SchedulerModule {

    @Singleton
    @Provides
    fun getScheduler(): BaseSchedulerProvider {
        return SchedulerProvider()
    }
}