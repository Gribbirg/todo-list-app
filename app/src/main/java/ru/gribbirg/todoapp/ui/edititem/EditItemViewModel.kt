package ru.gribbirg.todoapp.ui.edititem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.gribbirg.todoapp.TodoApplication
import ru.gribbirg.todoapp.data.data.TodoItem
import ru.gribbirg.todoapp.data.repositories.TodoItemRepository

class EditItemViewModel(
    private val todoItemRepository: TodoItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<EditItemUiState>(EditItemUiState.Loading)
    val uiState: StateFlow<EditItemUiState> = _uiState

    fun setItem(itemId: String?) {
        viewModelScope.launch {
            try {
                val item = itemId?.let { todoItemRepository.getItem(itemId) }
                _uiState.emit(
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
                )
            } catch (e: Exception) {
                _uiState.emit(EditItemUiState.Error(e))
            }
        }
    }

    fun save() {
        viewModelScope.launch {
            if (uiState.value is EditItemUiState.Loaded) {
                val state = (uiState.value as EditItemUiState.Loaded)
                when (state.itemState) {
                    EditItemUiState.ItemState.EDIT -> todoItemRepository.saveItem(item = state.item)
                    EditItemUiState.ItemState.NEW -> todoItemRepository.addItem(item = state.item)
                }
            }
        }
    }

    fun edit(item: TodoItem) {
        if (uiState.value is EditItemUiState.Loaded) {
            _uiState.update {
                val state = it as EditItemUiState.Loaded
                state.copy(
                    item = item
                )
            }
        }
    }

    fun delete() {
        require(uiState.value is EditItemUiState.Loaded)
        viewModelScope.launch {
            todoItemRepository.deleteItem((uiState.value as EditItemUiState.Loaded).item)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val todoItemRepository =
                    (this[APPLICATION_KEY] as TodoApplication).todoItemRepository
                EditItemViewModel(
                    todoItemRepository = todoItemRepository
                )
            }
        }
    }
}