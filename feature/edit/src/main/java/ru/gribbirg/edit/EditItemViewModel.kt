package ru.gribbirg.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.gribbirg.domain.model.todo.TodoItem
import ru.gribbirg.domain.repositories.TodoItemRepository
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

/**
 * View model for edit screen
 *
 * @see EditItemScreen
 * @see EditItemUiState
 */
class EditItemViewModel @AssistedInject constructor(
    @Assisted itemId: String?,
    private val todoItemRepository: TodoItemRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<EditItemUiState>(EditItemUiState.Loading)
    internal val uiState: StateFlow<EditItemUiState> = _uiState

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _uiState.update { EditItemUiState.Error(exception) }
    }

    init {
        setItem(itemId)
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

    internal fun save() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val state = uiState.value
            if (state is EditItemUiState.Loaded) {
                when (state.itemState) {
                    EditItemUiState.ItemState.EDIT ->
                        todoItemRepository.saveItem(
                            item = state.item.copy(
                                editDate = LocalDateTime.now(ZoneId.of("UTC"))
                            )
                        )

                    EditItemUiState.ItemState.NEW ->
                        todoItemRepository.addItem(
                            item = state.item.copy(
                                id = UUID.randomUUID().toString(),
                                creationDate = LocalDateTime.now(ZoneId.of("UTC")),
                                editDate = LocalDateTime.now(ZoneId.of("UTC"))
                            )
                        )
                }
            }
        }
    }

    internal fun edit(item: TodoItem) {
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

    @AssistedFactory
    interface Factory {
        fun create(itemId: String?): EditItemViewModel
    }
}