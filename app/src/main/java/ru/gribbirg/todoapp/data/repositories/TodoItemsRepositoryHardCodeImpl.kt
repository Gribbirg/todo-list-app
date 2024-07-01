package ru.gribbirg.todoapp.data.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import ru.gribbirg.todoapp.data.data.TodoImportance
import ru.gribbirg.todoapp.data.data.TodoItem
import java.time.LocalDate

class TodoItemsRepositoryHardCodeImpl(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TodoItemRepository {

    private val _itemsFlow = MutableStateFlow(defaultItems)

    override fun getItemsFlow(): StateFlow<List<TodoItem>> = _itemsFlow.asStateFlow()

    override suspend fun getItem(id: String): TodoItem? = withContext(dispatcher) {
        return@withContext _itemsFlow.value.firstOrNull { it.id == id }
    }

    override suspend fun addItem(item: TodoItem) = withContext(dispatcher) {
        _itemsFlow.update { state ->
            state + listOf(
                item.copy(
                    id = ((state.maxOfOrNull { item -> item.id.toLong() }
                        ?: 0L) + 1L).toString(),
                    creationDate = LocalDate.now()
                )
            )
        }
    }

    override suspend fun saveItem(item: TodoItem) = withContext(dispatcher) {
        _itemsFlow.update { state ->
            state.map {
                if (it.id == item.id)
                    item.copy(editDate = LocalDate.now())
                else
                    it
            }
        }
    }

    override suspend fun deleteItem(itemId: String) = withContext(dispatcher) {
        _itemsFlow.update { state -> state.filter { it.id != itemId } }
    }

    companion object {
        val defaultItems: List<TodoItem>
            get() {
                return List(30) {
                    TodoItem(
                        id = it.toString(),
                        text = "Дело $it ".repeat(it * 5 % 15 + 1),
                        importance = TodoImportance.entries[it % 3],
                        deadline = if (it % 3 == 0)
                            null
                        else LocalDate.now().plusDays((-100L..100L).random()),
                        completed = false,
                        creationDate = LocalDate.now().minusDays((-0L..100L).random()),
                        editDate = if (it % 5 == 0)
                            null
                        else LocalDate.now(),
                    )
                }
            }
    }
}