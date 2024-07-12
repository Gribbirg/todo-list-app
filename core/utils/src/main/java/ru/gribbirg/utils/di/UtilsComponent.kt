package ru.gribbirg.utils.di

import dagger.Component
import ru.gribbirg.utils.DataStoreSaver
import ru.gribbirg.utils.SystemDataProviderImpl
import ru.gribbirg.utils.di.modules.KeyValueSaverModule
import ru.gribbirg.utils.di.modules.SystemDataProviderModule

@Component(
    dependencies = [UtilsDependencies::class],
    modules = [KeyValueSaverModule::class, SystemDataProviderModule::class]
)
internal interface UtilsComponent {
    @Component.Factory
    interface Factory {
        fun create(deps: UtilsDependencies): UtilsComponent
    }

    val keyValueSaverFactory: DataStoreSaver.Factory

    val systemDataProviderImpl: SystemDataProviderImpl
}