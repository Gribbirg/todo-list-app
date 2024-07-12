package ru.gribbirg.network.di

import ru.gribbirg.network.ItemsApiClient

class NetworkFactory(
    deps: NetworkDependencies,
) {
    private val component: NetworkComponent = DaggerNetworkComponent.factory().create(deps)

    fun createApiClient(): ItemsApiClient = component.apiClientImpl
}