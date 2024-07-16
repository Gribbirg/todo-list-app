package ru.gribbirg.settings.di.modules

import dagger.Module
import dagger.Provides
import ru.gribbirg.domain.utils.SettingsHandler
import ru.gribbirg.utils.di.UtilsFactory

@Module
internal interface UtilsModule {
    companion object {
        @Provides
        fun settingsHandler(factory: UtilsFactory): SettingsHandler =
            factory.createSettingsHandler()
    }
}