package ru.gribbirg.todoapp.ui.todoitemslist

import androidx.annotation.StringRes
import ru.gribbirg.todoapp.data.data.TodoItem

sealed class TodoItemsListUiState {
    data object Loading : TodoItemsListUiState()

    data class Error(val exception: Throwable) : TodoItemsListUiState()

    data class Loaded(
        val items: List<TodoItem>,
        val filterState: FilterState,
        val doneCount: Int,
        val isUpdating: Boolean = false,
    ) : TodoItemsListUiState()

    enum class FilterState(val filter: (TodoItem) -> Boolean) {
        ALL({ true }),
        NOT_COMPLETED({ !it.completed })
    }
}

sealed class TodoItemsListUiEvent {
    data class NetworkError(override val time: Long, @StringRes val textId: Int) : TodoItemsListUiEvent()

    abstract val time: Long
}