package ru.gribbirg.todoapp.di.modules

import dagger.Module
import dagger.Provides
import ru.gribbirg.settings.SettingsViewModel
import ru.gribbirg.settings.di.SettingsFeatureFactory
import ru.gribbirg.todoapp.di.AppComponent

@Module
internal interface SettingsFeatureModule {
    companion object {
        @Provides
        fun settingsFeatureFactory(depsImpl: AppComponent): SettingsFeatureFactory =
            SettingsFeatureFactory(depsImpl)

        @Provides
        fun settingsViewModel(factory: SettingsFeatureFactory): SettingsViewModel =
            factory.createViewModel()
    }
}