package ru.gribbirg.todoapp.data.repositories

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.StateFlow
import ru.gribbirg.todoapp.data.data.TodoItem

interface TodoItemRepository {
    fun getItems(): List<TodoItem>

    suspend fun addItem(item: TodoItem)

    fun getItemsFlow():  StateFlow<SnapshotStateList<TodoItem>>

    suspend fun saveItem(item: TodoItem)

    suspend fun deleteItem(item: TodoItem)
}