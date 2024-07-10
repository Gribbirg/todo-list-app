package ru.gribbirg.todoapp.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Qualifier

@Qualifier
annotation class BackgroundDispatcher

@Qualifier
annotation class BackgroundOneThreadDispatcher

@Module
interface CoroutineModule {
    companion object {
        @Provides
        @BackgroundDispatcher
        fun backgroundDispatcher(): CoroutineDispatcher = Dispatchers.IO

        @OptIn(ExperimentalCoroutinesApi::class)
        @Provides
        @BackgroundOneThreadDispatcher
        fun backgroundOneThreadDispatcher(@BackgroundDispatcher backgroundDispatcher: CoroutineDispatcher): CoroutineDispatcher =
            backgroundDispatcher.limitedParallelism(1)
    }
}