package ru.gribbirg.network.di

import io.ktor.client.engine.HttpClientEngine
import ru.gribbirg.domain.utils.KeyValueDataSaver

interface NetworkDependencies {
    val keyValueDataSaver: KeyValueDataSaver
    val httpClient: HttpClientEngine
}