package ru.gribbirg.todoapp.di

import dagger.Subcomponent
import ru.gribbirg.settings.SettingsViewModel
import ru.gribbirg.todoapp.di.modules.SettingsFeatureModule

@Subcomponent(modules = [SettingsFeatureModule::class])
internal interface SettingsFeatureComponent {
    val settingsViewModel: SettingsViewModel
}