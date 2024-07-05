package ru.gribbirg.todoapp.ui.screens.edititem

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.gribbirg.todoapp.TodoApplication
import ru.gribbirg.todoapp.data.data.TodoItem
import ru.gribbirg.todoapp.data.repositories.items.TodoItemRepository
import ru.gribbirg.todoapp.ui.navigation.Screen
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

/**
 * View model for edit screen
 *
 * @see EditItemScreen
 * @see EditItemUiState
 */
class EditItemViewModel(
    savedStateHandle: SavedStateHandle,
    private val todoItemRepository: TodoItemRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<EditItemUiState>(EditItemUiState.Loading)
    val uiState: StateFlow<EditItemUiState> = _uiState

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _uiState.update { EditItemUiState.Error(exception) }
    }

    init {
        setItem(savedStateHandle[Screen.Edit.arguments.first().name])
    }

    private fun setItem(itemId: String?) {
        viewModelScope.launch(coroutineExceptionHandler) {
            val item = itemId?.let { todoItemRepository.getItem(itemId) }
            _uiState.update {
                if (item == null)
                    EditItemUiState.Loaded(
                        TodoItem(),
                        EditItemUiState.ItemState.NEW
                    )
                else
                    EditItemUiState.Loaded(
                        item,
                        EditItemUiState.ItemState.EDIT
                    )
            }
        }
    }

    fun save() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val state = uiState.value
            if (state is EditItemUiState.Loaded) {
                when (state.itemState) {
                    EditItemUiState.ItemState.EDIT ->
                        todoItemRepository.saveItem(
                            item = state.item.copy(
                                editDate = LocalDateTime.now(ZoneId.of("UTC"))
                            )
                        )

                    EditItemUiState.ItemState.NEW ->
                        todoItemRepository.addItem(
                            item = state.item.copy(
                                id = UUID.randomUUID().toString(),
                                creationDate = LocalDateTime.now(ZoneId.of("UTC")),
                                editDate = LocalDateTime.now(ZoneId.of("UTC"))
                            )
                        )
                }
            }
        }
    }

    fun edit(item: TodoItem) {
        viewModelScope.launch(coroutineExceptionHandler) {
            if (uiState.value is EditItemUiState.Loaded) {
                _uiState.update { state ->
                    if (state is EditItemUiState.Loaded)
                        state.copy(
                            item = item
                        )
                    else
                        state
                }
            }
        }
    }

    fun delete() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val state = uiState.value
            if (state is EditItemUiState.Loaded)
                todoItemRepository.deleteItem(state.item.id)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val todoItemRepository =
                    (this[APPLICATION_KEY] as TodoApplication).todoItemRepository
                val savedStateHandle = createSavedStateHandle()
                EditItemViewModel(
                    todoItemRepository = todoItemRepository,
                    savedStateHandle = savedStateHandle
                )
            }
        }
    }
}