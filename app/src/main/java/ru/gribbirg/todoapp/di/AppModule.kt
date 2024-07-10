package ru.gribbirg.todoapp.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.gribbirg.todoapp.data.keyvaluesaver.DataStoreSaver
import ru.gribbirg.todoapp.data.keyvaluesaver.KeyValueDataSaver
import ru.gribbirg.todoapp.data.logic.ItemsListsMerger
import ru.gribbirg.todoapp.data.logic.listMergerImpl
import ru.gribbirg.todoapp.data.repositories.items.TodoItemRepository
import ru.gribbirg.todoapp.data.repositories.items.TodoItemRepositoryImpl
import ru.gribbirg.todoapp.data.repositories.login.LoginRepository
import ru.gribbirg.todoapp.data.repositories.login.LoginRepositoryImpl
import ru.gribbirg.todoapp.data.systemdata.SystemDataProvider
import ru.gribbirg.todoapp.data.systemdata.SystemDataProviderImpl
import javax.inject.Qualifier

@Qualifier
annotation class SystemDataKeyValueSaver

@Module
interface AppModule {

    @Binds
    fun systemDataProvider(impl: SystemDataProviderImpl): SystemDataProvider

    @Binds
    fun itemsRepository(impl: TodoItemRepositoryImpl): TodoItemRepository

    @Binds
    fun loginRepository(impl: LoginRepositoryImpl): LoginRepository

    companion object {
        @Provides
        fun itemsMerger(): ItemsListsMerger = listMergerImpl

        @Provides
        @SystemDataKeyValueSaver
        fun systemDataKeyValueSaver(factory: DataStoreSaver.Factory): KeyValueDataSaver =
            factory.create(SystemDataProviderImpl.SAVER_NAME)
    }
}