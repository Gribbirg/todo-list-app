package ru.gribbirg.todoapp.ui.screens.todoitemslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.yandex.authsdk.YandexAuthResult
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
import ru.gribbirg.todoapp.R
import ru.gribbirg.todoapp.TodoApplication
import ru.gribbirg.todoapp.data.data.TodoItem
import ru.gribbirg.todoapp.data.repositories.items.NetworkState
import ru.gribbirg.todoapp.data.repositories.items.TodoItemRepository
import ru.gribbirg.todoapp.data.repositories.login.LoginRepository
import ru.gribbirg.todoapp.utils.toTimestamp
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * View model for list screen
 *
 * @see TodoListItemScreen
 * @see TodoItemsListUiState
 */
class TodoItemsListViewModel(
    private val todoItemRepository: TodoItemRepository,
    private val loginRepository: LoginRepository,
) : ViewModel() {

    private val filterFlow =
        MutableStateFlow(TodoItemsListUiState.ListState.FilterState.NOT_COMPLETED)
    private val _uiState = MutableStateFlow(TodoItemsListUiState())
    val uiState: StateFlow<TodoItemsListUiState> = _uiState.asStateFlow()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _uiState.update { state ->
            state.copy(
                listState = TodoItemsListUiState.ListState.Error(exception)
            )
        }
    }

    init {
        checkLogin()
        collectItemsStateFlow()
        collectNetworkStateFlow()
    }

    private fun checkLogin() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val isLogin = loginRepository.isLogin()
            _uiState.update { state ->
                state.copy(
                    loginState = if (isLogin)
                        TodoItemsListUiState.LoginState.Auth
                    else
                        TodoItemsListUiState.LoginState.Unauthorized
                )
            }
        }
    }

    private fun collectItemsStateFlow() {
        viewModelScope.launch(coroutineExceptionHandler) {
            todoItemRepository
                .getItemsFlow()
                .combine<
                        List<TodoItem>,
                        TodoItemsListUiState.ListState.FilterState,
                        TodoItemsListUiState.ListState
                        >(filterFlow) { list, filter ->
                    TodoItemsListUiState.ListState.Loaded(
                        items = list.filter(filter.filter).sortedWith(TodoItem.COMPARATOR_FOR_UI),
                        filterState = filter,
                        doneCount = list.count { it.completed }
                    )
                }
                .catch { e ->
                    emit(TodoItemsListUiState.ListState.Error(e))
                }
                .stateIn(
                    viewModelScope,
                    SharingStarted.Eagerly,
                    TodoItemsListUiState.ListState.Loading
                )
                .collect { state ->
                    _uiState.update { oldState ->
                        oldState.copy(listState = state)
                    }
                }
        }
    }

    private fun collectNetworkStateFlow() {
        viewModelScope.launch(coroutineExceptionHandler) {
            todoItemRepository
                .getNetworkStateFlow()
                .stateIn(viewModelScope, SharingStarted.Eagerly, NetworkState.Success)
                .collect { networkState ->
                    when (networkState) {
                        is NetworkState.Success ->
                            _uiState.update { state ->
                                state.copy(networkState = TodoItemsListUiState.NetworkState.NotUpdating)
                            }

                        is NetworkState.Updating ->
                            _uiState.update { state ->
                                state.copy(networkState = TodoItemsListUiState.NetworkState.Updating)
                            }

                        is NetworkState.Error -> {
                            _uiState.update { state ->
                                state.copy(
                                    networkState = TodoItemsListUiState.NetworkState.Updating,
                                    eventState = TodoItemsListUiState.EventState.ShowSnackBar(
                                        LocalDateTime.now(ZoneId.of("UTC")).toTimestamp(),
                                        networkState.messageId
                                    ),
                                )
                            }
                        }
                    }
                }
        }
    }

    fun onChecked(item: TodoItem, checked: Boolean) {
        viewModelScope.launch(coroutineExceptionHandler) {
            if (uiState.value.listState is TodoItemsListUiState.ListState.Loaded) {
                val newItem =
                    item.copy(completed = checked, editDate = LocalDateTime.now(ZoneId.of("UTC")))
                todoItemRepository.saveItem(newItem)
            }
        }
    }

    fun delete(item: TodoItem) {
        viewModelScope.launch(coroutineExceptionHandler) {
            todoItemRepository.deleteItem(item.id)
        }
    }

    fun onFilterChange(filterState: TodoItemsListUiState.ListState.FilterState) {
        viewModelScope.launch(coroutineExceptionHandler) {
            filterFlow.update { filterState }
        }
    }

    fun onUpdate() {
        viewModelScope.launch(coroutineExceptionHandler) {
            todoItemRepository.updateItems()
        }
    }

    fun onResetEvent() {
        viewModelScope.launch(coroutineExceptionHandler) {
            _uiState.update { state ->
                state.copy(eventState = null)
            }
        }
    }

    fun onLogin(result: YandexAuthResult) {
        viewModelScope.launch(coroutineExceptionHandler) {
            when (result) {
                is YandexAuthResult.Success -> {
                    loginRepository.registerUser(result.token.value)
                    _uiState.update { state ->
                        state.copy(
                            loginState = TodoItemsListUiState.LoginState.Auth
                        )
                    }
                    todoItemRepository.updateItems()
                }

                is YandexAuthResult.Failure -> {
                    _uiState.update { state ->
                        state.copy(
                            eventState = TodoItemsListUiState.EventState.ShowSnackBar(
                                time = LocalDateTime.now(ZoneId.of("UTC")).toTimestamp(),
                                textId = R.string.auth_error,
                            )
                        )
                    }
                }

                YandexAuthResult.Cancelled -> {}
            }
        }
    }

    fun onExit() {
        viewModelScope.launch(coroutineExceptionHandler) {
            loginRepository.removeLogin()
            _uiState.update { state ->
                state.copy(
                    loginState = TodoItemsListUiState.LoginState.Unauthorized
                )
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as TodoApplication
                val todoItemRepository = app.todoItemRepository
                val loginRepository = app.loginRepository
                TodoItemsListViewModel(
                    todoItemRepository = todoItemRepository,
                    loginRepository = loginRepository,
                )
            }
        }
    }
}