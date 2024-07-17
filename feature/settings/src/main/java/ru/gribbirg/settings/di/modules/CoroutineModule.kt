package ru.gribbirg.settings.di.modules

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Qualifier
internal annotation class BackgroundDispatcher

@Module
internal interface CoroutineModule {
    companion object {
        @Provides
        @BackgroundDispatcher
        fun backGroundDispatcher(): CoroutineDispatcher = Dispatchers.IO
    }
}