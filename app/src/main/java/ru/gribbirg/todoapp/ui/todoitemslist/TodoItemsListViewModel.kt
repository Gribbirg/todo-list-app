package ru.gribbirg.todoapp.ui.todoitemslist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.gribbirg.todoapp.TodoApplication
import ru.gribbirg.todoapp.data.data.TodoItem
import ru.gribbirg.todoapp.data.repositories.TodoItemRepository

class TodoItemsListViewModel(
    private val todoItemRepository: TodoItemRepository
) : ViewModel() {

    private val filterFlow = MutableStateFlow(TodoItemsListUiState.FilterState.NOT_COMPLETED)
    val uiState: StateFlow<TodoItemsListUiState> =
        todoItemRepository.getItemsFlow()
            .combine<List<TodoItem>, TodoItemsListUiState.FilterState, TodoItemsListUiState>(
                filterFlow
            ) { list, filter ->
                TodoItemsListUiState.Loaded(
                    items = list.filter(filter.filter),
                    filterState = filter,
                    doneCount = list.count { it.completed }
                )
            }
            .catch { e ->
                Log.e("ListFlow", "Todo list flow error: ${e.message ?: e.cause}")
                emit(TodoItemsListUiState.Error(e))
            }
            .stateIn(viewModelScope, SharingStarted.Eagerly, TodoItemsListUiState.Loading)


    fun onChecked(item: TodoItem, checked: Boolean) {
        viewModelScope.launch {
            if (uiState.value is TodoItemsListUiState.Loaded) {
                val newItem = item.copy(completed = checked)
                todoItemRepository.saveItem(newItem)
            }
        }
    }

    fun delete(item: TodoItem) {
        viewModelScope.launch {
            todoItemRepository.deleteItem(item)
        }
    }

    fun onFilterChange(filterState: TodoItemsListUiState.FilterState) {
        viewModelScope.launch {
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