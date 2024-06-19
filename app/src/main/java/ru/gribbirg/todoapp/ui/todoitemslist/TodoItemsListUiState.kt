package ru.gribbirg.todoapp.ui.todoitemslist

import androidx.compose.runtime.snapshots.SnapshotStateList
import ru.gribbirg.todoapp.data.data.TodoItem

sealed class TodoItemsListUiState {
    data object Loading : TodoItemsListUiState()
    data class Error(val text: String) : TodoItemsListUiState()
    data class Loaded(val items: SnapshotStateList<TodoItem>) : TodoItemsListUiState()
}