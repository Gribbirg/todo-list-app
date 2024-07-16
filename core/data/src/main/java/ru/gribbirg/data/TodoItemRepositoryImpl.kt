package ru.gribbirg.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.gribbirg.data.di.DataScope
import ru.gribbirg.data.di.modules.BackgroundOneThreadDispatcher
import ru.gribbirg.db.ItemsLocalClient
import ru.gribbirg.db.toLocalDbItem
import ru.gribbirg.domain.model.NetworkState
import ru.gribbirg.domain.model.todo.TodoItem
import ru.gribbirg.domain.repositories.LoginRepository
import ru.gribbirg.domain.utils.ItemsListsMerger
import ru.gribbirg.domain.utils.SystemDataProvider
import ru.gribbirg.network.ApiResponse
import ru.gribbirg.network.ItemsApiClient
import ru.gribbirg.network.dto.toNetworkDto
import javax.inject.Inject

/**
 * Implementation with sync local and internet values
 *
 * @see TodoItemRepository
 * @see NetworkState
 */
@DataScope
class TodoItemRepositoryImpl @Inject constructor(
    private val localClient: ItemsLocalClient,
    private val apiClient: ItemsApiClient,
    private val systemDataProvider: SystemDataProvider,
    private val loginRepository: LoginRepository,
    private val listsMerger: ItemsListsMerger,
    @BackgroundOneThreadDispatcher private val dispatcher: CoroutineDispatcher,
) : ru.gribbirg.domain.repositories.TodoItemRepository {

    private val _networkStateFlow: MutableStateFlow<NetworkState> =
        MutableStateFlow(NetworkState.Success)

    override fun getItemsFlow(): Flow<List<TodoItem>> {
        CoroutineScope(dispatcher).launch {
            updateItems()
        }
        return localClient.getItemsFlow().map { list -> list.map { it.toTodoItem() } }
    }

    override fun getNetworkStateFlow(): Flow<NetworkState> =
        _networkStateFlow.asStateFlow()

    override suspend fun getItem(id: String): TodoItem? = withContext(dispatcher) {
        return@withContext localClient.getItem(id)?.toTodoItem()
    }

    override suspend fun addItem(item: TodoItem): Unit = withContext(dispatcher) {
        localClient.addItem(item.toLocalDbItem())
        makeRequest {
            apiClient.add(
                item.toNetworkDto(
                    systemDataProvider.getDeviceId()
                )
            )
        }
    }

    override suspend fun saveItem(item: TodoItem): Unit = withContext(dispatcher) {
        localClient.updateItem(item.toLocalDbItem())
        makeRequest { apiClient.update(item.toNetworkDto(systemDataProvider.getDeviceId())) }
    }

    override suspend fun deleteItem(itemId: String): Unit = withContext(dispatcher) {
        localClient.deleteItemById(itemId)
        makeRequest { apiClient.delete(itemId) }
    }

    override suspend fun updateItems(): Unit = withContext(dispatcher) {
        if (!isLogin())
            return@withContext

        _networkStateFlow.update { NetworkState.Updating }
        val lastUpdateTime = apiClient.lastUpdateTime
        val response = makeRequest { apiClient.getAll() } ?: return@withContext

        val internetData = response.list.map { it.toTodoItem() }
        val cacheData = localClient.getAll().map { it.toTodoItem() }
        val resList = listsMerger.mergeLists(
            internet = internetData,
            local = cacheData,
            lastUpdateTime = lastUpdateTime
        )

        localClient.refreshItems(resList.map { it.toLocalDbItem() })
        makeRequest { apiClient.refreshAll(resList.map { it.toNetworkDto(systemDataProvider.getDeviceId()) }) }
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

    private suspend fun isLogin() = loginRepository.isLogin()
}