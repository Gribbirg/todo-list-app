package ru.gribbirg.settings.di

import ru.gribbirg.data.di.DataFactory
import ru.gribbirg.utils.di.UtilsFactory

interface SettingsFeatureDependencies {
    val dataFactory: DataFactory
    val utilsFactory: UtilsFactory
}