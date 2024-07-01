package ru.gribbirg.todoapp

import android.app.Application
import ru.gribbirg.todoapp.data.db.TodoDatabase
import ru.gribbirg.todoapp.data.repositories.TodoItemRepository
import ru.gribbirg.todoapp.data.repositories.TodoItemRepositoryLocalDbImpl

class TodoApplication : Application() {
    val todoItemRepository: TodoItemRepository by lazy {
        TodoItemRepositoryLocalDbImpl(
            TodoDatabase.getInstance(
                applicationContext
            ).getTodoDao()
        )
    }
}