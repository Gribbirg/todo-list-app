package ru.gribbirg.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.gribbirg.domain.model.NetworkState
import ru.gribbirg.domain.model.todo.TodoItem
import ru.gribbirg.domain.repositories.LoginRepository
import ru.gribbirg.domain.repositories.TodoItemRepository
import ru.gribbirg.utils.extensions.currentLocalDateTimeAtUtc
import ru.gribbirg.utils.extensions.toTimestamp
import javax.inject.Inject

/**
 * View model for list screen
 *
 * @see TodoListItemScreen
 * @see TodoItemsListUiState
 */
class TodoItemsListViewModel @Inject constructor(
    private val todoItemRepository: TodoItemRepository,
    private val loginRepository: LoginRepository,
    private val comparator: Comparator<TodoItem>,
) : ViewModel() {

    private val filterFlow =
        MutableStateFlow(TodoItemsListUiState.ListState.FilterState.NOT_COMPLETED)
    private val _uiState = MutableStateFlow(TodoItemsListUiState())
    internal val uiState: StateFlow<TodoItemsListUiState> = _uiState.asStateFlow()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _uiState.update { state ->
            state.copy(
                listState = TodoItemsListUiState.ListState.Error(exception)
            )
        }
    }

    init {
        collectLoginStateFlow()
        collectItemsStateFlow()
        collectNetworkStateFlow()
    }

    private fun collectLoginStateFlow() {
        viewModelScope.launch(coroutineExceptionHandler) {
            loginRepository
                .getLoginFlow()
                .map { userData ->
                    userData?.let {
                        TodoItemsListUiState.LoginState.Auth
                    } ?: TodoItemsListUiState.LoginState.Unauthorized
                }
                .catch { e ->
                    _uiState.update { state ->
                        state.copy(
                            listState = TodoItemsListUiState.ListState.Error(e),
                            loginState = TodoItemsListUiState.LoginState.Loading,
                        )
                    }
                }
                .stateIn(
                    viewModelScope,
                    SharingStarted.Eagerly,
                    TodoItemsListUiState.LoginState.Loading
                )
                .collect { loginState ->
                    _uiState.update { state ->
                        state.copy(
                            loginState = loginState
                        )
                    }
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
                        items = list.filter(filter.filter)
                            .sortedWith(comparator),
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
                .stateIn(
                    viewModelScope,
                    SharingStarted.Eagerly,
                    NetworkState.Success
                )
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
                                        currentLocalDateTimeAtUtc.toTimestamp(),
                                        networkState.messageId
                                    ),
                                )
                            }
                        }
                    }
                }
        }
    }

    internal fun onChecked(item: TodoItem, checked: Boolean) {
        viewModelScope.launch(coroutineExceptionHandler) {
            if (uiState.value.listState is TodoItemsListUiState.ListState.Loaded) {
                val newItem =
                    item.copy(
                        completed = checked,
                        editDate = currentLocalDateTimeAtUtc
                    )
                todoItemRepository.saveItem(newItem)
            }
        }
    }

    internal fun delete(item: TodoItem) {
        viewModelScope.launch(coroutineExceptionHandler) {
            _uiState.update { state ->
                state.copy(
                    eventState = TodoItemsListUiState.EventState.ItemDeleted(
                        time = currentLocalDateTimeAtUtc.toTimestamp(),
                        item = item,
                    )
                )
            }
            todoItemRepository.deleteItem(item.id)
        }
    }

    fun deleteById(id: String?) {
        viewModelScope.launch(coroutineExceptionHandler) {
            if (id == null) return@launch
            delete(todoItemRepository.getItem(id) ?: return@launch)
        }
    }

    internal fun onFilterChange(filterState: TodoItemsListUiState.ListState.FilterState) {
        viewModelScope.launch(coroutineExceptionHandler) {
            filterFlow.update { filterState }
        }
    }

    internal fun onUpdate() {
        viewModelScope.launch(coroutineExceptionHandler) {
            todoItemRepository.updateItems()
        }
    }

    internal fun onResetEvent() {
        viewModelScope.launch(coroutineExceptionHandler) {
            _uiState.update { state ->
                state.copy(eventState = null)
            }
        }
    }

    internal fun onAdd(item: TodoItem) {
        viewModelScope.launch(coroutineExceptionHandler) {
            todoItemRepository.addItem(
                item.copy(
                    editDate = currentLocalDateTimeAtUtc,
                )
            )
        }
    }
}