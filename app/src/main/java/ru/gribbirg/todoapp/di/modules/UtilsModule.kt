package ru.gribbirg.todoapp.di.modules

import dagger.Module
import dagger.Provides
import ru.gribbirg.domain.utils.SettingsHandler
import ru.gribbirg.todoapp.di.AppComponent
import ru.gribbirg.todoapp.di.AppScope
import ru.gribbirg.utils.di.UtilsFactory

@Module
internal interface UtilsModule {
    companion object {
        @Provides
        @AppScope
        fun factory(depsImpl: AppComponent): UtilsFactory = UtilsFactory(depsImpl)

        @Provides
        fun settingsHandler(factory: UtilsFactory): SettingsHandler =
            factory.createSettingsHandler()
    }
}