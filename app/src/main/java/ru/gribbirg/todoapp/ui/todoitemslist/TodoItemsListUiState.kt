package ru.gribbirg.todoapp.ui.todoitemslist

import androidx.annotation.StringRes
import ru.gribbirg.todoapp.data.data.TodoItem

data class TodoItemsListUiState(
    val listState: ListUiState = ListUiState.Loading,
    val eventState: UiEvent? = null,
    val networkState: NetworkState = NetworkState.NotUpdating,
    val loginState: LoginState = LoginState.Loading,
) {
    sealed class ListUiState {
        data object Loading : ListUiState()

        data class Error(val exception: Throwable) : ListUiState()

        data class Loaded(
            val items: List<TodoItem>,
            val filterState: FilterState,
            val doneCount: Int,
        ) : ListUiState()

        enum class FilterState(val filter: (TodoItem) -> Boolean) {
            ALL({ true }),
            NOT_COMPLETED({ !it.completed })
        }
    }

    sealed class UiEvent {
        data class ShowSnackBar(override val time: Long, @StringRes val textId: Int) : UiEvent()

        abstract val time: Long
    }

    sealed class NetworkState {
        data object Updating : NetworkState()
        data object NotUpdating : NetworkState()
    }

    sealed class LoginState {
        data object Loading: LoginState()
        data object Auth : LoginState()
        data object Unauthorized : LoginState()
    }
}