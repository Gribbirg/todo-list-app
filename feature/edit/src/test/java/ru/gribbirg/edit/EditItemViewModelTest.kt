package ru.gribbirg.edit

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.yield
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.gribbirg.domain.model.todo.TodoItem
import ru.gribbirg.domain.repositories.TodoItemRepository

class EditItemViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setDispatchers() {
        Dispatchers.setMain(dispatcher)
    }

    @Test
    fun `init new item`() = runTest(dispatcher) {
        val id = null
        val repo: TodoItemRepository = mockk()

        val viewModel = EditItemViewModel(id, repo)
        repeat(5) { yield() }

        assertTrue(viewModel.uiState.value is EditItemUiState.Loaded)
        assertEquals(
            EditItemUiState.ItemState.NEW,
            (viewModel.uiState.value as EditItemUiState.Loaded).itemState
        )
    }

    @Test
    fun `init edit item`() = runTest(dispatcher) {
        val id = "1"
        val item = TodoItem(id = id)
        val repo: TodoItemRepository = mockk {
            coEvery { getItem(id) } returns item
        }

        val viewModel = EditItemViewModel(id, repo)
        repeat(5) { yield() }

        assertEquals(
            EditItemUiState.Loaded(item, EditItemUiState.ItemState.EDIT),
            viewModel.uiState.value
        )
    }

    @Test
    fun `save new item`() = runTest(dispatcher) {
        val id = null
        val repo: TodoItemRepository = mockk(relaxed = true)
        val viewModel = EditItemViewModel(id, repo)

        viewModel.save()
        yield()

        coVerify { repo.addItem(any()) }
    }

    @Test
    fun `save edited item`() = runTest(dispatcher) {
        val id = "1"
        val repo: TodoItemRepository = mockk(relaxed = true) {
            coEvery { getItem(id) } returns TodoItem(id)
        }
        val viewModel = EditItemViewModel(id, repo)

        viewModel.save()
        yield()

        coVerify { repo.saveItem(any()) }
    }

    @Test
    fun `edit item`() = runTest(dispatcher) {
        val id = "1"
        val item = TodoItem(id = id)
        val editedItem = item.copy(text = "new text")
        val repo: TodoItemRepository = mockk {
            coEvery { getItem(id) } returns item
        }
        val viewModel = EditItemViewModel(id, repo)

        viewModel.edit(editedItem)
        yield()

        assertEquals(
            EditItemUiState.Loaded(editedItem, EditItemUiState.ItemState.EDIT),
            viewModel.uiState.value
        )
    }
}