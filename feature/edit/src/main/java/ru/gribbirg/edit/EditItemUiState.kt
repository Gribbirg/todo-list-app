package ru.gribbirg.edit

import ru.gribbirg.domain.model.TodoItem

/**
 * Ui states for edit screen
 *
 * @see EditItemScreen
 * @see EditItemViewModel
 */
internal sealed class EditItemUiState {
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