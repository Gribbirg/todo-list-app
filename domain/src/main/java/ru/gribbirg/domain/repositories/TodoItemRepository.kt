package ru.gribbirg.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.gribbirg.domain.model.NetworkState
import ru.gribbirg.domain.model.todo.TodoItem

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