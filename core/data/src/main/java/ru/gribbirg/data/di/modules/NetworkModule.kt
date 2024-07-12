package ru.gribbirg.data.di.modules

import dagger.Module
import dagger.Provides
import ru.gribbirg.data.di.DataComponent
import ru.gribbirg.network.ItemsApiClient
import ru.gribbirg.network.di.NetworkFactory

@Module
internal interface NetworkModule {
    companion object {
        @Provides
        fun networkFactory(depsImpl: DataComponent): NetworkFactory = NetworkFactory(depsImpl)

        @Provides
        fun networkClient(factory: NetworkFactory): ItemsApiClient = factory.createApiClient()
    }
}