package ru.gribbirg.data.di.modules

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Qualifier

@Qualifier
annotation class BackgroundOneThreadDispatcher

@Qualifier
annotation class BackgroundDispatcher

@Module
internal interface CoroutinesModule {
    companion object {
        @Provides
        @BackgroundDispatcher
        fun backgroundDispatcher(): CoroutineDispatcher =
            Dispatchers.IO

        @OptIn(ExperimentalCoroutinesApi::class)
        @Provides
        @BackgroundOneThreadDispatcher
        fun backgroundOneTreadDispatcher(): CoroutineDispatcher =
            Dispatchers.IO.limitedParallelism(1)
    }
}