package ru.gribbirg.todoapp

import android.app.Application
import ru.gribbirg.todoapp.data.repositories.TodoItemRepository
import ru.gribbirg.todoapp.data.repositories.TodoItemsRepositoryHardCodeImpl

class TodoApplication: Application() {
    val todoItemRepository: TodoItemRepository by lazy { TodoItemsRepositoryHardCodeImpl() }
}