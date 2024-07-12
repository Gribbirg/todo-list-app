package ru.gribbirg.db

import kotlinx.coroutines.flow.Flow

interface ItemsLocalClient {
    fun getAll(): List<TodoDbEntity>

    fun getItemsFlow(): Flow<List<TodoDbEntity>>

    suspend fun getItem(id: String): TodoDbEntity?

    suspend fun updateItem(item: TodoDbEntity)

    suspend fun addItem(item: TodoDbEntity)

    suspend fun deleteItemById(id: String)

    suspend fun addAll(items: List<TodoDbEntity>)

    suspend fun deleteAll()

    suspend fun refreshItems(items: List<TodoDbEntity>)
}