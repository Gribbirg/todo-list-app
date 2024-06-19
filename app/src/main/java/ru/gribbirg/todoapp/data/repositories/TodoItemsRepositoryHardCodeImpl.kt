package ru.gribbirg.todoapp.data.repositories

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.gribbirg.todoapp.data.data.TodoImportance
import ru.gribbirg.todoapp.data.data.TodoItem
import java.time.LocalDate

class TodoItemsRepositoryHardCodeImpl : TodoItemRepository {

    private val itemsFlow = MutableStateFlow(mutableStateListOf(*defaultItems))

    private val items = itemsFlow.value

    override fun getItems(): List<TodoItem> = items.toList()

    override suspend fun getItemsFlow(): StateFlow<SnapshotStateList<TodoItem>> = itemsFlow

    override suspend fun addItem(item: TodoItem) {
        val newItem = item.copy(id = ((items.last().id).toLong() + 1).toString())
        itemsFlow.emit(items.also { it.add(newItem) })
    }

    override suspend fun saveItem(item: TodoItem) {
        val index = items.indexOfLast { it.id == item.id }
        itemsFlow.emit(items.also { it[index] = item })
    }

    override suspend fun deleteItem(item: TodoItem) {
        itemsFlow.emit(items.also { it.remove(item) })
    }

    companion object {
        val defaultItems
            get() = Array(30) {
                TodoItem(
                    id = it.toString(),
                    text = "Дело 1",
                    importance = TodoImportance.MEDIUM,
                    deadline = LocalDate.now(),
                    completed = false,
                    creationDate = LocalDate.now(),
                    editDate = LocalDate.now(),
                )
            }
    }
}