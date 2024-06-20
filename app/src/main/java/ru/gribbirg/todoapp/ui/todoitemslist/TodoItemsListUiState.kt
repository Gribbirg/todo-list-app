package ru.gribbirg.todoapp.ui.todoitemslist

import ru.gribbirg.todoapp.data.data.TodoItem

sealed class TodoItemsListUiState {
    data object Loading : TodoItemsListUiState()
    data class Error(val exception: Throwable) : TodoItemsListUiState()

    data class Loaded(
        val items: List<TodoItem>,
        val filterState: FilterState,
        val doneCount: Int
    ) : TodoItemsListUiState()

    enum class FilterState(val filter: (TodoItem) -> Boolean) {
        ALL({ true }),
        NOT_COMPLETED({ !it.completed })
    }
}