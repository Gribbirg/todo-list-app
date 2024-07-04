package ru.gribbirg.todoapp

import android.app.Application
import ru.gribbirg.todoapp.data.datestore.DataStoreUtil
import ru.gribbirg.todoapp.data.db.TodoDatabase
import ru.gribbirg.todoapp.data.repositories.TodoItemRepository
import ru.gribbirg.todoapp.data.repositories.TodoItemRepositoryImpl
import ru.gribbirg.todoapp.network.ApiClient

class TodoApplication : Application() {
    val todoItemRepository: TodoItemRepository by lazy {
        TodoItemRepositoryImpl(
            TodoDatabase.getInstance(
                applicationContext
            ).getTodoDao(),
            ApiClient(
                dataStore = DataStoreUtil(applicationContext, "internet_data")
            ),
            applicationContext
        )
    }
}