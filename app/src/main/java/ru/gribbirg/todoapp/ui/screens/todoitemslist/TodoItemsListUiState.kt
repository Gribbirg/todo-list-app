package ru.gribbirg.todoapp.ui.screens.todoitemslist

import androidx.annotation.StringRes
import ru.gribbirg.todoapp.data.data.TodoItem

/**
 * Ui state of list screen
 *
 * @see TodoListItemScreen
 * @see TodoItemsListViewModel
 */
data class TodoItemsListUiState(
    val listState: ListState = ListState.Loading,
    val eventState: EventState? = null,
    val networkState: NetworkState = NetworkState.NotUpdating,
    val loginState: LoginState = LoginState.Loading,
) {
    sealed class ListState {
        data object Loading : ListState()

        data class Error(val exception: Throwable) : ListState()

        data class Loaded(
            val items: List<TodoItem>,
            val filterState: FilterState,
            val doneCount: Int,
        ) : ListState()

        enum class FilterState(val filter: (TodoItem) -> Boolean) {
            ALL({ true }),
            NOT_COMPLETED({ !it.completed })
        }
    }

    sealed class EventState {
        data class ShowSnackBar(override val time: Long, @StringRes val textId: Int) : EventState()

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