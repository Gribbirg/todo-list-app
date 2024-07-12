package ru.gribbirg.data.di.modules

import dagger.Module
import dagger.Provides
import ru.gribbirg.data.di.DataComponent
import ru.gribbirg.db.ItemsLocalClient
import ru.gribbirg.db.di.DatabaseFactory

@Module
internal interface DatabaseModule {
    companion object {
        @Provides
        fun databaseFactory(depsImpl: DataComponent): DatabaseFactory = DatabaseFactory(depsImpl)

        @Provides
        fun localClient(factory: DatabaseFactory): ItemsLocalClient = factory.createLocalClient()
    }
}