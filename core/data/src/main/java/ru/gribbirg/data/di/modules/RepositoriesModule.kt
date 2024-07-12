package ru.gribbirg.data.di.modules

import dagger.Binds
import dagger.Module
import ru.gribbirg.data.LoginRepositoryImpl
import ru.gribbirg.data.TodoItemRepositoryImpl
import ru.gribbirg.domain.repositories.LoginRepository
import ru.gribbirg.domain.repositories.TodoItemRepository

@Module
interface RepositoriesModule {
    @Binds
    fun todoItemsRepository(impl: TodoItemRepositoryImpl): TodoItemRepository

    @Binds
    fun loginRepository(impl: LoginRepositoryImpl): LoginRepository
}