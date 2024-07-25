package ru.gribbirg.list

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.yield
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.gribbirg.domain.model.NetworkState
import ru.gribbirg.domain.model.todo.TodoItem
import ru.gribbirg.domain.model.user.UserData
import ru.gribbirg.domain.repositories.LoginRepository
import ru.gribbirg.domain.repositories.TodoItemRepository

class TodoItemsListViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setDispatchers() {
        Dispatchers.setMain(dispatcher)
    }

    @Test
    fun `collect login successful state`() = runTest(dispatcher) {
        val login: LoginRepository = mockk {
            every { getLoginFlow() } returns MutableStateFlow(UserData())
        }

        val viewModel = TodoItemsListViewModel(
            loginRepository = login,
            todoItemRepository = mockk(relaxed = true),
            comparator = mockk(relaxed = true),
        )
        repeat(5) { yield() }

        assertEquals(TodoItemsListUiState.LoginState.Auth, viewModel.uiState.value.loginState)
    }

    @Test
    fun `collect login unauthorized state`() = runTest(dispatcher) {
        val login: LoginRepository = mockk {
            every { getLoginFlow() } returns MutableStateFlow(null)
        }

        val viewModel = TodoItemsListViewModel(
            loginRepository = login,
            todoItemRepository = mockk(relaxed = true),
            comparator = mockk(relaxed = true),
        )
        repeat(5) { yield() }

        assertEquals(
            TodoItemsListUiState.LoginState.Unauthorized,
            viewModel.uiState.value.loginState
        )
    }

    @Test
    fun `collect login error state`() = runTest(dispatcher) {
        val login: LoginRepository = mockk {
            every { getLoginFlow() } returns flow { throw Exception() }
        }

        val viewModel = TodoItemsListViewModel(
            loginRepository = login,
            todoItemRepository = mockk(relaxed = true),
            comparator = mockk(relaxed = true),
        )
        repeat(5) { yield() }

        assertTrue(viewModel.uiState.value.listState is TodoItemsListUiState.ListState.Error)
    }

    @Test
    fun `collect network success state`() = runTest(dispatcher) {
        val itemsRepo: TodoItemRepository = mockk(relaxed = true) {
            every { getNetworkStateFlow() } returns MutableStateFlow(NetworkState.Success)
        }

        val viewModel = TodoItemsListViewModel(
            loginRepository = mockk(relaxed = true),
            todoItemRepository = itemsRepo,
            comparator = mockk(relaxed = true),
        )
        repeat(5) { yield() }

        assertEquals(
            TodoItemsListUiState.NetworkState.NotUpdating,
            viewModel.uiState.value.networkState
        )
    }

    @Test
    fun `collect network updating state`() = runTest(dispatcher) {
        val itemsRepo: TodoItemRepository = mockk(relaxed = true) {
            every { getNetworkStateFlow() } returns MutableStateFlow(NetworkState.Updating)
        }

        val viewModel = TodoItemsListViewModel(
            loginRepository = mockk(relaxed = true),
            todoItemRepository = itemsRepo,
            comparator = mockk(relaxed = true),
        )
        repeat(5) { yield() }

        assertEquals(
            TodoItemsListUiState.NetworkState.Updating,
            viewModel.uiState.value.networkState
        )
    }

    @Test
    fun `collect network error state`() = runTest(dispatcher) {
        val itemsRepo: TodoItemRepository = mockk(relaxed = true) {
            every { getNetworkStateFlow() } returns MutableStateFlow(NetworkState.Error.NetworkError)
        }

        val viewModel = TodoItemsListViewModel(
            loginRepository = mockk(relaxed = true),
            todoItemRepository = itemsRepo,
            comparator = mockk(relaxed = true),
        )
        repeat(5) { yield() }

        assertEquals(
            TodoItemsListUiState.NetworkState.Updating,
            viewModel.uiState.value.networkState
        )
        assertTrue(
            viewModel.uiState.value.eventState is TodoItemsListUiState.EventState.ShowSnackBar
        )
    }

    @Test
    fun `collect list state`() = runTest(dispatcher) {
        val list = listOf(TodoItem(), TodoItem())
        val itemsRepo: TodoItemRepository = mockk(relaxed = true) {
            every { getItemsFlow() } returns MutableStateFlow(list)
        }

        val viewModel = TodoItemsListViewModel(
            loginRepository = mockk(relaxed = true),
            todoItemRepository = itemsRepo,
            comparator = mockk(relaxed = true),
        )
        repeat(5) { yield() }

        assertEquals(
            TodoItemsListUiState.ListState.Loaded(
                list,
                TodoItemsListUiState.ListState.FilterState.NOT_COMPLETED,
                0
            ),
            viewModel.uiState.value.listState
        )
    }

    @Test
    fun `collect list state with error`() = runTest(dispatcher) {
        val exception = Exception()
        val itemsRepo: TodoItemRepository = mockk(relaxed = true) {
            every { getItemsFlow() } returns flow { throw exception }
        }

        val viewModel = TodoItemsListViewModel(
            loginRepository = mockk(relaxed = true),
            todoItemRepository = itemsRepo,
            comparator = mockk(relaxed = true),
        )
        repeat(5) { yield() }

        assertTrue(
            viewModel.uiState.value.listState is TodoItemsListUiState.ListState.Error
        )
    }

    @Test
    fun `hide item by filter`() = runTest(dispatcher) {
        val list = listOf(TodoItem(completed = true), TodoItem())
        val itemsRepo: TodoItemRepository = mockk(relaxed = true) {
            every { getItemsFlow() } returns MutableStateFlow(list)
        }
        val viewModel = TodoItemsListViewModel(
            loginRepository = mockk(relaxed = true),
            todoItemRepository = itemsRepo,
            comparator = mockk(relaxed = true),
        )
        repeat(5) { yield() }


        assertEquals(
            TodoItemsListUiState.ListState.Loaded(
                listOf(list[1]),
                TodoItemsListUiState.ListState.FilterState.NOT_COMPLETED,
                1
            ),
            viewModel.uiState.value.listState
        )
    }

    @Test
    fun `change filter`() = runTest(dispatcher) {
        val list = listOf(TodoItem(completed = true), TodoItem())
        val itemsRepo: TodoItemRepository = mockk(relaxed = true) {
            every { getItemsFlow() } returns MutableStateFlow(list)
        }
        val viewModel = TodoItemsListViewModel(
            loginRepository = mockk(relaxed = true),
            todoItemRepository = itemsRepo,
            comparator = mockk(relaxed = true),
        )

        viewModel.onFilterChange(TodoItemsListUiState.ListState.FilterState.ALL)
        repeat(5) { yield() }

        assertEquals(
            TodoItemsListUiState.ListState.Loaded(
                list,
                TodoItemsListUiState.ListState.FilterState.ALL,
                1
            ),
            viewModel.uiState.value.listState
        )
    }

    @Test
    fun `on completed change`() = runTest(dispatcher) {
        val list = listOf(TodoItem(completed = true), TodoItem())
        val itemsRepo: TodoItemRepository = mockk(relaxed = true) {
            every { getItemsFlow() } returns MutableStateFlow(list)
        }
        val viewModel = TodoItemsListViewModel(
            loginRepository = mockk(relaxed = true),
            todoItemRepository = itemsRepo,
            comparator = mockk(relaxed = true),
        )
        repeat(5) { yield() }

        viewModel.onChecked(TodoItem(), true)
        repeat(5) { yield() }

        coVerify { itemsRepo.saveItem(any()) }
    }

    @Test
    fun `on delete`() = runTest(dispatcher) {
        val id = "1"
        val item = TodoItem(id)
        val itemsRepo: TodoItemRepository = mockk(relaxed = true) {
            every { getItemsFlow() } returns MutableStateFlow(listOf(item))
        }
        val viewModel = TodoItemsListViewModel(
            loginRepository = mockk(relaxed = true),
            todoItemRepository = itemsRepo,
            comparator = mockk(relaxed = true),
        )

        viewModel.delete(item)
        repeat(5) { yield() }

        assertTrue(
            viewModel.uiState.value.eventState is TodoItemsListUiState.EventState.ItemDeleted
        )
        assertEquals(
            item,
            (viewModel.uiState.value.eventState as TodoItemsListUiState.EventState.ItemDeleted).item
        )
        coVerify { itemsRepo.deleteItem(id) }
    }

    @Test
    fun `on update`() = runTest(dispatcher) {
        val itemsRepo: TodoItemRepository = mockk(relaxed = true)
        val viewModel = TodoItemsListViewModel(
            loginRepository = mockk(relaxed = true),
            todoItemRepository = itemsRepo,
            comparator = mockk(relaxed = true),
        )

        viewModel.onUpdate()
        repeat(5) { yield() }

        coVerify { itemsRepo.updateItems() }
    }

    @Test
    fun `on add item`() = runTest(dispatcher) {
        val item = TodoItem()
        val itemsRepo: TodoItemRepository = mockk(relaxed = true) {
            every { getItemsFlow() } returns MutableStateFlow(listOf())
        }
        val viewModel = TodoItemsListViewModel(
            loginRepository = mockk(relaxed = true),
            todoItemRepository = itemsRepo,
            comparator = mockk(relaxed = true),
        )

        viewModel.onAdd(item)
        yield()

        coVerify { itemsRepo.addItem(any()) }
    }

    @Test
    fun `on reset event`() = runTest(dispatcher) {
        val item = TodoItem()
        val itemsRepo: TodoItemRepository = mockk(relaxed = true) {
            every { getItemsFlow() } returns MutableStateFlow(listOf())
        }
        val viewModel = TodoItemsListViewModel(
            loginRepository = mockk(relaxed = true),
            todoItemRepository = itemsRepo,
            comparator = mockk(relaxed = true),
        )
        viewModel.delete(item)

        viewModel.onResetEvent()
        yield()

        assertNull(viewModel.uiState.value.eventState)
    }
}