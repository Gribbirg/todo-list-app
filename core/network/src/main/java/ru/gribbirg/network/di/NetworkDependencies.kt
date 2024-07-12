package ru.gribbirg.network.di

import ru.gribbirg.domain.utils.KeyValueDataSaver

interface NetworkDependencies {
    val keyValueDataSaver: KeyValueDataSaver
}