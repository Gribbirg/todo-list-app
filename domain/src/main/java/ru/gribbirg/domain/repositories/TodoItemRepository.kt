package ru.gribbirg.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.gribbirg.domain.model.NetworkState

/**
 * Repository for getting items
 */
interface TodoItemRepository {
    fun getItemsFlow(): Flow<List<ru.gribbirg.domain.model.TodoItem>>

    fun getNetworkStateFlow(): Flow<NetworkState>

    suspend fun getItem(id: String): ru.gribbirg.domain.model.TodoItem?

    suspend fun addItem(item: ru.gribbirg.domain.model.TodoItem)

    suspend fun saveItem(item: ru.gribbirg.domain.model.TodoItem)

    suspend fun deleteItem(itemId: String)

    suspend fun updateItems()
}