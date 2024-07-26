package ru.gribbirg.todoapp.di.modules

import dagger.Module
import dagger.Provides
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android

@Module
interface NetworkModule {
    companion object {
        @Provides
        fun httpClient(): HttpClientEngine =
            Android.create()
    }
}