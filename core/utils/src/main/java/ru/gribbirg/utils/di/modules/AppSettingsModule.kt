package ru.gribbirg.utils.di.modules

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import ru.gribbirg.domain.utils.KeyValueDataSaver
import ru.gribbirg.utils.DataStoreSaver
import ru.gribbirg.utils.SettingsHandlerImpl
import javax.inject.Qualifier
import javax.inject.Scope

@Scope
annotation class AppSettingsScope

@Qualifier
internal annotation class AppSettingsQualifier

@Module
internal interface AppSettingsModule {
    companion object {
        @Provides
        @AppSettingsScope
        @AppSettingsQualifier
        fun keyValueDataSaver(factory: DataStoreSaver.Factory): KeyValueDataSaver =
            factory.create(SettingsHandlerImpl.SETTINGS_KEY)

        @Provides
        @AppSettingsScope
        @AppSettingsQualifier
        fun coroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO
    }
}