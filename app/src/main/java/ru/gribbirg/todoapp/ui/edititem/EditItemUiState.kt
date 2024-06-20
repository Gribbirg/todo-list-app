package ru.gribbirg.todoapp.ui.edititem

import ru.gribbirg.todoapp.data.data.TodoItem

sealed class EditItemUiState {
    data object Loading : EditItemUiState()
    data class Error(val exception: Throwable) : EditItemUiState()
    data class Loaded(
        val item: TodoItem,
        val itemState: ItemState
    ) : EditItemUiState()

    enum class ItemState {
        NEW,
        EDIT
    }
}