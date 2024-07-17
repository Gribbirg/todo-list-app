package ru.gribbirg.settings.di

import dagger.Component
import ru.gribbirg.settings.SettingsViewModel
import ru.gribbirg.settings.di.modules.CoroutineModule
import ru.gribbirg.settings.di.modules.RepositoryModule
import ru.gribbirg.settings.di.modules.UtilsModule

@Component(
    dependencies = [SettingsFeatureDependencies::class],
    modules = [UtilsModule::class, RepositoryModule::class, CoroutineModule::class],
)
internal interface SettingsFeatureComponent {
    @Component.Factory
    interface Factory {
        fun create(dependencies: SettingsFeatureDependencies): SettingsFeatureComponent
    }

    val viewModel: SettingsViewModel
}