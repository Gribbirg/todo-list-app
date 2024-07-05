package ru.gribbirg.todoapp.data.repositories.items

import android.content.Context
import android.provider.Settings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import ru.gribbirg.todoapp.data.keyvaluesaver.DataStoreSaver
import ru.gribbirg.todoapp.data.logic.ItemsListsMerger
import ru.gribbirg.todoapp.data.network.ApiResponse
import ru.gribbirg.todoapp.data.network.ItemsApiClientImpl
import ru.gribbirg.todoapp.data.network.dto.toNetworkDto

/**
 * Implementation with sync local and internet values
 *
 * @see TodoItemRepository
 * @see NetworkState
 */
class TodoItemRepositoryImpl @OptIn(ExperimentalCoroutinesApi::class) constructor(
    private val todoDao: TodoDao,
    private val apiClientImpl: ItemsApiClientImpl,
    private val context: Context,
    private val internetDataStore: DataStoreSaver,
    private val listsMerger: ItemsListsMerger,
    private val dispatcher: CoroutineDispatcher =
        Dispatchers.IO.limitedParallelism(1),
) : TodoItemRepository {

    private val _networkStateFlow: MutableStateFlow<NetworkState> =
        MutableStateFlow(NetworkState.Success)

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
            apiClientImpl.add(
                item.toNetworkDto(
                    getDeviceId()
                )
            )
        }
    }

    override suspend fun saveItem(item: TodoItem): Unit = withContext(dispatcher) {
        todoDao.updateItem(item.toLocalDbItem())
        makeRequest { apiClientImpl.update(item.toNetworkDto(getDeviceId())) }
    }

    override suspend fun deleteItem(itemId: String): Unit = withContext(dispatcher) {
        todoDao.deleteItemById(itemId)
        makeRequest { apiClientImpl.delete(itemId) }
    }

    override suspend fun updateItems(): Unit = withContext(dispatcher) {
        if (!isLogin())
            return@withContext

        _networkStateFlow.update { NetworkState.Updating }
        val lastUpdateTime = apiClientImpl.lastUpdateTime
        val response = makeRequest { apiClientImpl.getAll() } ?: return@withContext

        val internetData = response.list.map { it.toTodoItem() }
        val cacheData = todoDao.getAll().map { it.toTodoItem() }
        val resList = listsMerger.mergeLists(
            internet = internetData,
            local = cacheData,
            lastUpdateTime = lastUpdateTime
        )

        todoDao.refreshItems(resList.map { it.toLocalDbItem() })
        makeRequest { apiClientImpl.refreshAll(resList.map { it.toNetworkDto(getDeviceId()) }) }
    }


    private suspend fun <T> makeRequest(request: suspend () -> ApiResponse<T>): T? =
        withContext(dispatcher) {
            if (!isLogin()) {
                return@withContext null
            }
            when (val response = request()) {
                is ApiResponse.Success -> {
                    _networkStateFlow.update { NetworkState.Success }
                    return@withContext response.body
                }

                is ApiResponse.Error.WrongRevision -> {
                    updateItems()
                    return@withContext makeRequest(request)
                }

                is ApiResponse.Error.NetworkError -> {
                    _networkStateFlow.update { NetworkState.Error.NetworkError }
                    return@withContext null
                }

                is ApiResponse.Error.NotFound -> {
                    updateItems()
                    return@withContext null
                }

                is ApiResponse.Error -> {
                    _networkStateFlow.update { NetworkState.Error.UnknownError }
                    return@withContext null
                }
            }
        }

    private suspend fun isLogin() = internetDataStore.get("user_api_key") != null

    private fun getDeviceId() = Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ANDROID_ID
    )
}