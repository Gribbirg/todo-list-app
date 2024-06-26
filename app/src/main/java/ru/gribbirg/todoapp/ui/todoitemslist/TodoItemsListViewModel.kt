package ru.gribbirg.todoapp.ui.todoitemslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.gribbirg.todoapp.TodoApplication
import ru.gribbirg.todoapp.data.data.TodoItem
import ru.gribbirg.todoapp.data.repositories.TodoItemRepository

class TodoItemsListViewModel(
    private val todoItemRepository: TodoItemRepository
) : ViewModel() {

    private val filterFlow = MutableStateFlow(TodoItemsListUiState.FilterState.NOT_COMPLETED)
    private val _uiState = MutableStateFlow<TodoItemsListUiState>(TodoItemsListUiState.Loading)
    val uiState: StateFlow<TodoItemsListUiState> = _uiState.asStateFlow()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _uiState.update { TodoItemsListUiState.Error(exception) }
    }

    init {
        viewModelScope.launch(coroutineExceptionHandler) {
            todoItemRepository
                .getItemsFlow()
                .combine<
                        List<TodoItem>,
                        TodoItemsListUiState.FilterState,
                        TodoItemsListUiState,
                        >(filterFlow) { list, filter ->
                    TodoItemsListUiState.Loaded(
                        items = list.filter(filter.filter),
                        filterState = filter,
                        doneCount = list.count { it.completed }
                    )
                }
                .catch { e ->
                    emit(TodoItemsListUiState.Error(e))
                }
                .stateIn(viewModelScope, SharingStarted.Eagerly, TodoItemsListUiState.Loading)
                .collect { state ->
                    _uiState.update { state }
                }
        }
    }

    fun onChecked(item: TodoItem, checked: Boolean) {
        viewModelScope.launch(coroutineExceptionHandler) {
            if (uiState.value is TodoItemsListUiState.Loaded) {
                val newItem = item.copy(completed = checked)
                todoItemRepository.saveItem(newItem)
            }
        }
    }

    fun delete(item: TodoItem) {
        viewModelScope.launch(coroutineExceptionHandler) {
            todoItemRepository.deleteItem(item.id)
        }
    }

    fun onFilterChange(filterState: TodoItemsListUiState.FilterState) {
        viewModelScope.launch(coroutineExceptionHandler) {
            filterFlow.emit(filterState)
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