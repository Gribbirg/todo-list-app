package ru.gribbirg.todoapp.data.repositories

import kotlinx.coroutines.flow.StateFlow
import ru.gribbirg.todoapp.data.data.TodoItem

interface TodoItemRepository {
    fun getItemsFlow(): StateFlow<List<TodoItem>>

    suspend fun getItem(id: String): TodoItem?

    suspend fun addItem(item: TodoItem)

    suspend fun saveItem(item: TodoItem)

    suspend fun deleteItem(item: TodoItem)
}