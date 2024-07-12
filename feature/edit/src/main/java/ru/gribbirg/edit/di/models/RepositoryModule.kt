package ru.gribbirg.edit.di.models

import dagger.Module
import dagger.Provides
import ru.gribbirg.data.di.DataFactory
import ru.gribbirg.domain.repositories.TodoItemRepository

@Module
internal interface RepositoryModule {
    companion object {
        @Provides
        fun repository(factory: DataFactory): TodoItemRepository = factory.createItemsRepository()
    }
}