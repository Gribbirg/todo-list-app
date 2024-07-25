package ru.gribbirg.data

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import ru.gribbirg.db.ItemsLocalClient
import ru.gribbirg.db.TodoDbEntity
import ru.gribbirg.db.toLocalDbItem
import ru.gribbirg.domain.model.NetworkState
import ru.gribbirg.domain.model.todo.TodoItem
import ru.gribbirg.domain.repositories.LoginRepository
import ru.gribbirg.domain.utils.ItemsListsMerger
import ru.gribbirg.domain.utils.SystemDataProvider
import ru.gribbirg.network.ApiResponse
import ru.gribbirg.network.ItemsApiClient
import ru.gribbirg.network.dto.TodoItemResponseDto
import ru.gribbirg.network.dto.TodoListResponseDto
import ru.gribbirg.network.dto.toNetworkDto
import ru.gribbirg.utils.extensions.currentLocalDateTimeAtUtc
import ru.gribbirg.utils.extensions.toLocalDateTime
import ru.gribbirg.utils.extensions.toTimestamp
import java.util.UUID

class TodoItemRepositoryImplTest {

    private val dispatcher = StandardTestDispatcher()

    private fun createTodoItem() = TodoItem(
            id = UUID.randomUUID().toString(),
            creationDate = currentLocalDateTimeAtUtc.toTimestamp().toLocalDateTime()!!,
            editDate = currentLocalDateTimeAtUtc.toTimestamp().toLocalDateTime()!!,
        )

    private fun mockLocalClient(id: String, item: TodoDbEntity?): ItemsLocalClient =
        mockk(relaxed = true) {
            coEvery { getItem(id) } returns item
        }

    private fun mockSystemDataProvider(deviceId: String): SystemDataProvider =
        mockk {
            coEvery { getDeviceId() } returns deviceId
        }

    private fun mockLoginRepository(isLogin: Boolean): LoginRepository =
        mockk {
            coEvery { this@mockk.isLogin() } returns isLogin
        }

    @Test
    fun `get item`() = runTest(dispatcher) {
        val id = "1"
        val item = TodoItem()
        val local = mockLocalClient(id, item.toLocalDbItem())
        val repo = TodoItemRepositoryImpl(
            localClient = local,
            apiClient = mockk(),
            systemDataProvider = mockk(),
            loginRepository = mockk(),
            listsMerger = mockk(),
            dispatcher = dispatcher,
        )

        val res = repo.getItem(id)

        assertEquals(item, res)
    }

    @Test
    fun `get not exists item`() = runTest(dispatcher) {
        val id = "1"
        val local = mockLocalClient(id, null)
        val repo = TodoItemRepositoryImpl(
            localClient = local,
            apiClient = mockk(),
            systemDataProvider = mockk(),
            loginRepository = mockk(),
            listsMerger = mockk(),
            dispatcher = dispatcher,
        )

        val res = repo.getItem(id)

        assertNull(res)
    }

    @Test
    fun `add item with login`() = runTest(dispatcher) {
        val item = TodoItem()
        val deviceId = "1"
        val itemDto = item.toNetworkDto(deviceId)
        val local: ItemsLocalClient = mockk(relaxed = true)
        val client: ItemsApiClient = mockk(relaxed = true) {
            coEvery { add(itemDto) } returns ApiResponse.Success(
                TodoItemResponseDto(
                    status = "ok",
                    revision = 1,
                    element = itemDto
                )
            )
        }
        val repo = TodoItemRepositoryImpl(
            localClient = local,
            apiClient = client,
            systemDataProvider = mockSystemDataProvider(deviceId),
            listsMerger = mockk(),
            loginRepository = mockLoginRepository(true),
            dispatcher = dispatcher,
        )

        repo.addItem(item)

        coVerify { local.addItem(item.toLocalDbItem()) }
        coVerify { client.add(itemDto) }
        assertEquals(NetworkState.Success, repo.getNetworkStateFlow().first())
    }

    @Test
    fun `add item without login`() = runTest(dispatcher) {
        val item = TodoItem()
        val deviceId = "1"
        val local: ItemsLocalClient = mockk(relaxed = true)
        val client: ItemsApiClient = mockk()
        val repo = TodoItemRepositoryImpl(
            localClient = local,
            apiClient = client,
            systemDataProvider = mockSystemDataProvider(deviceId),
            listsMerger = mockk(),
            loginRepository = mockLoginRepository(false),
            dispatcher = dispatcher,
        )

        repo.addItem(item)

        coVerify { local.addItem(item.toLocalDbItem()) }
    }

    @Test
    fun `add item with server network error`() = runTest(dispatcher) {
        val item = TodoItem()
        val deviceId = "1"
        val itemDto = item.toNetworkDto(deviceId)
        val local: ItemsLocalClient = mockk(relaxed = true)
        val client: ItemsApiClient = mockk(relaxed = true) {
            coEvery { add(itemDto) } returns ApiResponse.Error.NetworkError
        }
        val repo = TodoItemRepositoryImpl(
            localClient = local,
            apiClient = client,
            systemDataProvider = mockSystemDataProvider(deviceId),
            listsMerger = mockk(),
            loginRepository = mockLoginRepository(true),
            dispatcher = dispatcher,
        )

        repo.addItem(item)

        coVerify { local.addItem(item.toLocalDbItem()) }
        coVerify { client.add(itemDto) }
        assertEquals(NetworkState.Error.NetworkError, repo.getNetworkStateFlow().first())
    }

    @Test
    fun `add item with server unknown error`() = runTest(dispatcher) {
        val item = TodoItem()
        val deviceId = "1"
        val itemDto = item.toNetworkDto(deviceId)
        val local: ItemsLocalClient = mockk(relaxed = true)
        val client: ItemsApiClient = mockk(relaxed = true) {
            coEvery { add(itemDto) } returns ApiResponse.Error.UnknownError
        }
        val repo = TodoItemRepositoryImpl(
            localClient = local,
            apiClient = client,
            systemDataProvider = mockSystemDataProvider(deviceId),
            listsMerger = mockk(),
            loginRepository = mockLoginRepository(true),
            dispatcher = dispatcher,
        )

        repo.addItem(item)

        coVerify { local.addItem(item.toLocalDbItem()) }
        coVerify { client.add(itemDto) }
        assertEquals(NetworkState.Error.UnknownError, repo.getNetworkStateFlow().first())
    }

    @Test
    fun `update items`() = runTest(dispatcher) {
        val localItems = listOf(createTodoItem(), createTodoItem())
        val networkItems = listOf(createTodoItem(), createTodoItem())
        val mergedList = networkItems + localItems
        val deviceId = "1"
        val local: ItemsLocalClient = mockk(relaxed = true) {
            coEvery { getAll() } returns localItems.map { it.toLocalDbItem() }
        }
        val client: ItemsApiClient = mockk(relaxed = true) {
            coEvery { getAll() } returns ApiResponse.Success(
                TodoListResponseDto(
                    status = "ok",
                    revision = 1,
                    list = networkItems.map { it.toNetworkDto(deviceId) }),
            )
            coEvery { lastUpdateTime } returns 0L.toLocalDateTime()!!
        }
        val listMerger: ItemsListsMerger = mockk {
            every {
                mergeLists(
                    localItems,
                    networkItems,
                    0L.toLocalDateTime()!!
                )
            } returns mergedList
        }
        val repo = TodoItemRepositoryImpl(
            localClient = local,
            apiClient = client,
            systemDataProvider = mockSystemDataProvider(deviceId),
            listsMerger = listMerger,
            loginRepository = mockLoginRepository(true),
            dispatcher = dispatcher,
        )

        repo.updateItems()

        coVerify { local.refreshItems(mergedList.map { it.toLocalDbItem() }) }
        coVerify { client.refreshAll(mergedList.map { it.toNetworkDto(deviceId) }) }
    }


    @Test
    fun `save item with login`() = runTest(dispatcher) {
        val item = TodoItem()
        val deviceId = "1"
        val itemDto = item.toNetworkDto(deviceId)
        val local: ItemsLocalClient = mockk(relaxed = true)
        val client: ItemsApiClient = mockk(relaxed = true) {
            coEvery { update(itemDto) } returns ApiResponse.Success(
                TodoItemResponseDto(
                    status = "ok",
                    revision = 1,
                    element = itemDto
                )
            )
        }
        val repo = TodoItemRepositoryImpl(
            localClient = local,
            apiClient = client,
            systemDataProvider = mockSystemDataProvider(deviceId),
            listsMerger = mockk(),
            loginRepository = mockLoginRepository(true),
            dispatcher = dispatcher,
        )

        repo.saveItem(item)

        coVerify { local.updateItem(item.toLocalDbItem()) }
        coVerify { client.update(itemDto) }
        assertEquals(NetworkState.Success, repo.getNetworkStateFlow().first())
    }

    @Test
    fun `delete item with login`() = runTest(dispatcher) {
        val item = TodoItem()
        val deviceId = "1"
        val itemDto = item.toNetworkDto(deviceId)
        val local: ItemsLocalClient = mockk(relaxed = true)
        val client: ItemsApiClient = mockk(relaxed = true) {
            coEvery { delete(item.id) } returns ApiResponse.Success(
                TodoItemResponseDto(
                    status = "ok",
                    revision = 1,
                    element = itemDto
                )
            )
        }
        val repo = TodoItemRepositoryImpl(
            localClient = local,
            apiClient = client,
            systemDataProvider = mockSystemDataProvider(deviceId),
            listsMerger = mockk(),
            loginRepository = mockLoginRepository(true),
            dispatcher = dispatcher,
        )

        repo.deleteItem(item.id)

        coVerify { local.deleteItemById(item.id) }
        coVerify { client.delete(item.id) }
        assertEquals(NetworkState.Success, repo.getNetworkStateFlow().first())
    }
}