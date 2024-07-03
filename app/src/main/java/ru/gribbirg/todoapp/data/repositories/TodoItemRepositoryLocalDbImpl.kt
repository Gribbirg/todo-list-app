package ru.gribbirg.todoapp.data.repositories

import android.content.Context
import android.provider.Settings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.gribbirg.todoapp.data.data.TodoItem
import ru.gribbirg.todoapp.data.db.TodoDao
import ru.gribbirg.todoapp.data.db.toLocalDbItem
import ru.gribbirg.todoapp.network.ApiClient
import ru.gribbirg.todoapp.network.dto.toNetworkDto
import java.time.LocalDate
import java.util.UUID

class TodoItemRepositoryLocalDbImpl(
    private val todoDao: TodoDao,
    private val apiClient: ApiClient,
    private val context: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TodoItemRepository {

    override fun getItemsFlow(): Flow<List<TodoItem>> {
        CoroutineScope(dispatcher).launch {
            updateItems()
        }
        return todoDao.getItemsFlow().map { list -> list.map { it.toTodoItem() } }
    }

    override suspend fun getItem(id: String): TodoItem? = withContext(dispatcher) {
        return@withContext todoDao.getItem(id)?.toTodoItem()
    }

    override suspend fun addItem(item: TodoItem) = withContext(dispatcher) {
        val revision = updateItems()
        val itemCopy = item.copy(id = UUID.randomUUID().toString())
        todoDao.addItem(itemCopy.toLocalDbItem())
        apiClient.add(
            itemCopy.toNetworkDto(
                getDeviceId()
            ), revision
        )
    }

    override suspend fun saveItem(item: TodoItem) = withContext(dispatcher) {
        val revision = updateItems()
        val itemCopy = item.copy(editDate = LocalDate.now())
        todoDao.updateItem(itemCopy.toLocalDbItem())
        apiClient.update(itemCopy.toNetworkDto(getDeviceId()), revision)
    }

    override suspend fun deleteItem(itemId: String) = withContext(dispatcher) {
        val revision = updateItems()
        todoDao.deleteItemById(itemId)
        apiClient.delete(itemId, revision)
    }

    private suspend fun updateItems(): Int = withContext(dispatcher) {
        val response = apiClient.getAll()
        todoDao.deleteAll()
        todoDao.addAll(response.list.map { it.toTodoItem().toLocalDbItem() })
        return@withContext response.revision
    }

    private fun getDeviceId() = Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ANDROID_ID
    )
}