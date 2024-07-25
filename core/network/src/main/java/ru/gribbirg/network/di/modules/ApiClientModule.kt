package ru.gribbirg.network.di.modules

import dagger.Module
import dagger.Provides
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import ru.gribbirg.network.getMainHttpClient
import javax.inject.Qualifier
import javax.inject.Scope

@Scope
annotation class ApiClientScope

@Qualifier
annotation class ApiClientImplQualifier

@Module
internal interface ApiClientModule {
    companion object {
        @Provides
        @ApiClientImplQualifier
        fun httpClient(): HttpClient =
            getMainHttpClient(Android.create())
    }
}