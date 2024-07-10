package ru.gribbirg.todoapp.di.modules

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.gribbirg.todoapp.data.logic.ItemsListsMerger
import ru.gribbirg.todoapp.data.logic.listMergerImpl
import ru.gribbirg.todoapp.data.repositories.items.TodoItemRepository
import ru.gribbirg.todoapp.data.repositories.items.TodoItemRepositoryImpl
import ru.gribbirg.todoapp.data.repositories.login.LoginRepository
import ru.gribbirg.todoapp.data.repositories.login.LoginRepositoryImpl

@Module
interface AppModule {

    @Binds
    fun itemsRepository(impl: TodoItemRepositoryImpl): TodoItemRepository

    @Binds
    fun loginRepository(impl: LoginRepositoryImpl): LoginRepository

    companion object {
        @Provides
        fun itemsMerger(): ItemsListsMerger = listMergerImpl
    }
}