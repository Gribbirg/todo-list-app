package ru.gribbirg.todoapp.di.modules

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.gribbirg.todoapp.data.keyvaluesaver.DataStoreSaver
import ru.gribbirg.todoapp.data.keyvaluesaver.KeyValueDataSaver
import ru.gribbirg.todoapp.data.systemdata.SystemDataProvider
import ru.gribbirg.todoapp.data.systemdata.SystemDataProviderImpl
import javax.inject.Qualifier
import javax.inject.Scope

@Scope
annotation class SystemDataScope

@Qualifier
annotation class SystemDataKeyValueSaver

@Module
interface SystemDataModule {

    @Binds
    @SystemDataScope
    fun systemDataProvider(impl: SystemDataProviderImpl): SystemDataProvider

    companion object {
        @Provides
        @SystemDataKeyValueSaver
        fun systemDataKeyValueSaver(factory: DataStoreSaver.Factory): KeyValueDataSaver =
            factory.create(SystemDataProviderImpl.SAVER_NAME)
    }
}