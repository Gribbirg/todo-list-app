package ru.gribbirg.network.di

import dagger.Component
import ru.gribbirg.network.ItemsApiClientImpl
import ru.gribbirg.network.di.modules.ApiClientModule
import ru.gribbirg.network.di.modules.ApiClientScope

@ApiClientScope
@Component(dependencies = [NetworkDependencies::class], modules = [ApiClientModule::class])
internal interface NetworkComponent {
    @Component.Factory
    interface Factory {
        fun create(deps: NetworkDependencies): NetworkComponent
    }

    val apiClientImpl: ItemsApiClientImpl
}