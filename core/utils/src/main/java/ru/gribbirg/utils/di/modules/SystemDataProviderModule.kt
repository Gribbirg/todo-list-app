package ru.gribbirg.utils.di.modules

import dagger.Module
import dagger.Provides
import ru.gribbirg.domain.utils.KeyValueDataSaver
import ru.gribbirg.utils.DataStoreSaver
import ru.gribbirg.utils.SystemDataProviderImpl
import javax.inject.Qualifier

@Qualifier
internal annotation class SystemDataQualifier

@Module
internal interface SystemDataProviderModule {
    companion object {
        @Provides
        @SystemDataQualifier
        fun keyValueDataSaver(factory: DataStoreSaver.Factory): KeyValueDataSaver =
            factory.create(SystemDataProviderImpl.SAVER_NAME)
    }
}