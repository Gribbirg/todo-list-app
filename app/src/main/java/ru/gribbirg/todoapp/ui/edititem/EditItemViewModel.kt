package ru.gribbirg.todoapp.ui.edititem

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
import ru.gribbirg.todoapp.data.repositories.TodoItemRepository
import ru.gribbirg.todoapp.ui.navigation.Screen

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
            _uiState.update { EditItemUiState.Saving }
            if (state is EditItemUiState.Loaded) {
                when (state.itemState) {
                    EditItemUiState.ItemState.EDIT -> todoItemRepository.saveItem(item = state.item)
                    EditItemUiState.ItemState.NEW -> todoItemRepository.addItem(item = state.item)
                }
            }
            _uiState.update { EditItemUiState.Finish }
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