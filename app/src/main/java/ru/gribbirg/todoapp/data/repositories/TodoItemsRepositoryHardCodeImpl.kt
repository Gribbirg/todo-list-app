package ru.gribbirg.todoapp.data.repositories

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import ru.gribbirg.todoapp.data.data.TodoImportance
import ru.gribbirg.todoapp.data.data.TodoItem
import java.time.LocalDate

class TodoItemsRepositoryHardCodeImpl : TodoItemRepository {

    private val itemsFlow = MutableStateFlow(mutableStateListOf(*defaultItems))

    private val items = itemsFlow.value

    override fun getItems(): List<TodoItem> = items.toList()

    override fun getItemsFlow(): StateFlow<SnapshotStateList<TodoItem>> = itemsFlow

    override suspend fun addItem(item: TodoItem) {
        itemsFlow.update { state ->
            state.also {
                it.add(
                    item.copy(
                        id = (state.last().id.toLong() + 1L).toString(),
                        creationDate = LocalDate.now()
                    )
                )
            }.toMutableStateList()
        }
    }

    override suspend fun saveItem(item: TodoItem) {
        itemsFlow.update { state ->
            state.map {
                if (it.id == item.id)
                    item.copy(editDate = LocalDate.now())
                else
                    it
            }.toMutableStateList()
        }
    }

    override suspend fun deleteItem(item: TodoItem) {
        itemsFlow.update { state -> state.filter { it.id != item.id }.toMutableStateList() }
    }

    companion object {
        val defaultItems
            get() = Array(20) {
                TodoItem(
                    id = it.toString(),
                    text = "Дело $it",
                    importance = TodoImportance.entries[it % 3],
                    deadline = LocalDate.now(),
                    completed = false,
                    creationDate = LocalDate.now(),
                    editDate = LocalDate.now(),
                )
            }
    }
}