package ru.gribbirg.todoapp.data.repositories

import android.content.Context
import android.provider.Settings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.gribbirg.todoapp.data.data.TodoItem
import ru.gribbirg.todoapp.data.db.TodoDao
import ru.gribbirg.todoapp.data.db.toLocalDbItem
import ru.gribbirg.todoapp.network.ApiClient
import ru.gribbirg.todoapp.network.ApiResponse
import ru.gribbirg.todoapp.network.NetworkState
import ru.gribbirg.todoapp.network.dto.toNetworkDto
import java.time.LocalDateTime
import java.util.concurrent.Executors

class TodoItemRepositoryImpl(
    private val todoDao: TodoDao,
    private val apiClient: ApiClient,
    private val context: Context,
    private val dispatcher: CoroutineDispatcher =
        Executors.newSingleThreadExecutor().asCoroutineDispatcher(),
) : TodoItemRepository {

    private val _networkStateFlow: MutableStateFlow<NetworkState> =
        MutableStateFlow(NetworkState.Updating)

    override fun getItemsFlow(): Flow<List<TodoItem>> {
        CoroutineScope(dispatcher).launch {
            updateItems()
        }
        return todoDao.getItemsFlow().map { list -> list.map { it.toTodoItem() } }
    }

    override fun getNetworkStateFlow(): Flow<NetworkState> =
        _networkStateFlow.asStateFlow()

    override suspend fun getItem(id: String): TodoItem? = withContext(dispatcher) {
        return@withContext todoDao.getItem(id)?.toTodoItem()
    }

    override suspend fun addItem(item: TodoItem): Unit = withContext(dispatcher) {
        todoDao.addItem(item.toLocalDbItem())
        makeRequest {
            apiClient.add(
                item.toNetworkDto(
                    getDeviceId()
                )
            )
        }
    }

    override suspend fun saveItem(item: TodoItem): Unit = withContext(dispatcher) {
        todoDao.updateItem(item.toLocalDbItem())
        makeRequest { apiClient.update(item.toNetworkDto(getDeviceId())) }
    }

    override suspend fun deleteItem(itemId: String): Unit = withContext(dispatcher) {
        todoDao.deleteItemById(itemId)
        makeRequest { apiClient.delete(itemId) }
    }

    override suspend fun updateItems(): Unit = withContext(dispatcher) {
        _networkStateFlow.update { NetworkState.Updating }
        val lastUpdateTime = apiClient.lastUpdateTime
        val response = makeRequest { apiClient.getAll() } ?: return@withContext

        val internetData = response.list.map { it.toTodoItem() }
        val cacheData = todoDao.getAll().map { it.toTodoItem() }
        val resList = mergeLists(
            internetList = internetData,
            cacheList = cacheData,
            lastUpdateTime = lastUpdateTime
        )

        todoDao.refreshItems(resList.map { it.toLocalDbItem() })
        makeRequest { apiClient.refreshAll(resList.map { it.toNetworkDto(getDeviceId()) }) }
    }


    private suspend fun <T> makeRequest(request: suspend () -> ApiResponse<T>): T? {
        when (val response = request()) {
            is ApiResponse.Success -> {
                _networkStateFlow.update { NetworkState.Success }
                return response.body
            }

            is ApiResponse.Error.WrongRevisionError -> {
                updateItems()
                return makeRequest(request)
            }

            is ApiResponse.Error.NetworkError -> {
                _networkStateFlow.update { NetworkState.Error.NetworkError }
                return null
            }

            is ApiResponse.Error -> {
                _networkStateFlow.update { NetworkState.Error.UnknownError }
                return null
            }
        }
    }

    private fun getDeviceId() = Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ANDROID_ID
    )

    private fun mergeLists(
        internetList: List<TodoItem>,
        cacheList: List<TodoItem>,
        lastUpdateTime: LocalDateTime,
    ): List<TodoItem> {
        val internetMap = internetList.associateBy { it.id }
        val cacheMap = cacheList.associateBy { it.id }

        val res = mutableListOf<TodoItem>()

        cacheMap.forEach { (key, cacheValue) ->
            if (key in internetMap.keys) {
                val internetValue = internetMap[key]!!
                val lastUpdatedItem =
                    if (cacheValue.editDate >= internetValue.editDate)
                        cacheValue
                    else
                        internetValue
                res.add(lastUpdatedItem)
            }
        }

        cacheMap.forEach { (key, cacheValue) ->
            if (key !in internetMap.keys && cacheValue.editDate >= lastUpdateTime) {
                res.add(cacheValue)
            }
        }

        internetMap.forEach { (key, internetValue) ->
            if (key !in cacheMap.keys && internetValue.editDate >= lastUpdateTime) {
                res.add(internetValue)
            }
        }

        return res
    }
}