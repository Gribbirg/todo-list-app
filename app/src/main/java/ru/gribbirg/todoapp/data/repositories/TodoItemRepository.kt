package ru.gribbirg.todoapp.data.repositories

import kotlinx.coroutines.flow.StateFlow
import ru.gribbirg.todoapp.data.data.TodoItem

interface TodoItemRepository {
    suspend fun getItems(): List<TodoItem>

    suspend fun addItem(item: TodoItem)

    val itemsFlow: StateFlow<List<TodoItem>>

    suspend fun saveItem(item: TodoItem)

    suspend fun deleteItem(item: TodoItem)
}