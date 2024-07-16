package ru.gribbirg.settings.di

import ru.gribbirg.settings.SettingsViewModel

class SettingsFeatureFactory(deps: SettingsFeatureDependencies) {
    private val component: SettingsFeatureComponent =
        DaggerSettingsFeatureComponent.factory().create(deps)

    fun createViewModel(): SettingsViewModel = component.viewModel
}