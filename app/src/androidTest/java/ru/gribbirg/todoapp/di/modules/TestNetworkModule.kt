package ru.gribbirg.todoapp.di.modules

import dagger.Module
import dagger.Provides
import io.ktor.client.engine.HttpClientEngine
import ru.gribbirg.todoapp.network.mockEngine

@Module
internal interface TestNetworkModule {
    companion object {
        @Provides
        fun httpClientEngine(): HttpClientEngine = mockEngine
    }
}