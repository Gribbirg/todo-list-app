package ru.gribbirg.todoapp.ui.todoitemslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.gribbirg.todoapp.TodoApplication
import ru.gribbirg.todoapp.data.data.TodoItem
import ru.gribbirg.todoapp.data.repositories.TodoItemRepository

class TodoItemsListViewModel(
    private val todoItemRepository: TodoItemRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<TodoItemsListUiState>(TodoItemsListUiState.Loading)
    val uiState: StateFlow<TodoItemsListUiState> = _uiState

    init {
        viewModelScope.launch {
            todoItemRepository.getItemsFlow().collect {
                _uiState.emit(TodoItemsListUiState.Loaded(items = it))
            }
        }
    }

    fun onChecked(item: TodoItem, checked: Boolean) {
        viewModelScope.launch {
            if (uiState.value is TodoItemsListUiState.Loaded) {
                val newItem = item.copy(completed = checked)
                todoItemRepository.saveItem(newItem)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val todoItemRepository =
                    (this[APPLICATION_KEY] as TodoApplication).todoItemRepository
                TodoItemsListViewModel(
                    todoItemRepository = todoItemRepository
                )
            }
        }
    }
}