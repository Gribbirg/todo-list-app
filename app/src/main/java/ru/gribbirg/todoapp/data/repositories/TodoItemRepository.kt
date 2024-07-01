package ru.gribbirg.todoapp.data.repositories

import kotlinx.coroutines.flow.Flow
import ru.gribbirg.todoapp.data.data.TodoItem

interface TodoItemRepository {
    fun getItemsFlow(): Flow<List<TodoItem>>

    suspend fun getItem(id: String): TodoItem?

    suspend fun addItem(item: TodoItem)

    suspend fun saveItem(item: TodoItem)

    suspend fun deleteItem(item: TodoItem)
}