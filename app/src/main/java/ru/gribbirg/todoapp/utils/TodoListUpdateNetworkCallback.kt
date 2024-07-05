package ru.gribbirg.todoapp.utils

import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.gribbirg.todoapp.data.repositories.TodoItemRepository
import kotlin.coroutines.CoroutineContext

class TodoListUpdateNetworkCallback(
    private val todoItemRepository: TodoItemRepository,
    private val coroutineContext: CoroutineContext = Dispatchers.IO,
) : ConnectivityManager.NetworkCallback() {

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        CoroutineScope(coroutineContext).launch {
            todoItemRepository.updateItems()
        }
    }

}