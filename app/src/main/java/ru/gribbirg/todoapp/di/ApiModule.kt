package ru.gribbirg.todoapp.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import ru.gribbirg.todoapp.data.keyvaluesaver.DataStoreSaver
import ru.gribbirg.todoapp.data.keyvaluesaver.KeyValueDataSaver
import ru.gribbirg.todoapp.data.network.ItemsApiClient
import ru.gribbirg.todoapp.data.network.ItemsApiClientImpl
import ru.gribbirg.todoapp.data.network.NetworkConstants
import ru.gribbirg.todoapp.data.network.mainHttpClient
import javax.inject.Qualifier
import javax.inject.Scope

@Qualifier
annotation class ApiClientKeyValueSaverQualifier

@Scope
annotation class ApiClientScope

@Module
interface ApiModule {
    @Binds
    fun apiClient(impl: ItemsApiClientImpl): ItemsApiClient

    companion object {
        @Provides
        @ApiClientKeyValueSaverQualifier
        @ApiClientScope
        fun apiClientDataSaver(factory: DataStoreSaver.Factory) : KeyValueDataSaver =
            factory.create(NetworkConstants.KEY_VALUE_SAVER_NAME)

        @Provides
        @ApiClientScope
        fun httpClient(): HttpClient = mainHttpClient
    }
}