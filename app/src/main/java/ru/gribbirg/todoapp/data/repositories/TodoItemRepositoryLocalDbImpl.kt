package ru.gribbirg.todoapp.data.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.gribbirg.todoapp.data.data.TodoItem
import ru.gribbirg.todoapp.data.db.TodoDao
import ru.gribbirg.todoapp.data.db.toLocalDbItem
import java.util.UUID

class TodoItemRepositoryLocalDbImpl(
    private val todoDao: TodoDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TodoItemRepository {

    override fun getItemsFlow(): Flow<List<TodoItem>> {
        return todoDao.getItemsFlow().map { list -> list.map { it.toTodoItem() } }
    }

    override suspend fun getItem(id: String): TodoItem? = withContext(dispatcher) {
        return@withContext todoDao.getItem(id)?.toTodoItem()
    }

    override suspend fun addItem(item: TodoItem) = withContext(dispatcher) {
        todoDao.addItem(item.toLocalDbItem().copy(id = UUID.randomUUID().toString()))
    }

    override suspend fun saveItem(item: TodoItem) = withContext(dispatcher) {
        todoDao.updateItem(item.toLocalDbItem())
    }

    override suspend fun deleteItem(item: TodoItem) = withContext(dispatcher) {
        todoDao.deleteItem(item.toLocalDbItem())
    }
}