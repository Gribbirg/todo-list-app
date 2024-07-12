package ru.gribbirg.list.di.modules

import dagger.Module
import dagger.Provides
import ru.gribbirg.data.di.DataFactory
import ru.gribbirg.domain.repositories.LoginRepository
import ru.gribbirg.domain.repositories.TodoItemRepository

@Module
interface RepositoryModule {
    companion object {
        @Provides
        fun itemsRepository(factory: DataFactory): TodoItemRepository =
            factory.createItemsRepository()

        @Provides
        fun loginRepository(factory: DataFactory): LoginRepository =
            factory.createLoginRepository()
    }
}