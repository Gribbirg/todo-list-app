package ru.gribbirg.todoapp.data.repositories.items

import kotlinx.coroutines.flow.Flow
import ru.gribbirg.todoapp.data.data.TodoItem

/**
 * Repository for getting items
 */
interface TodoItemRepository {
    fun getItemsFlow(): Flow<List<TodoItem>>

    fun getNetworkStateFlow(): Flow<NetworkState>

    suspend fun getItem(id: String): TodoItem?

    suspend fun addItem(item: TodoItem)

    suspend fun saveItem(item: TodoItem)

    suspend fun deleteItem(itemId: String)

    suspend fun updateItems()
}