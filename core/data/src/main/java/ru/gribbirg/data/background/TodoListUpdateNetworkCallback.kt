package ru.gribbirg.data.background

import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.gribbirg.data.di.modules.BackgroundDispatcher
import javax.inject.Inject

/**
 * Network callback. Make data sync on internet connection available
 */
class TodoListUpdateNetworkCallback @Inject constructor(
    private val todoItemRepository: ru.gribbirg.domain.repositories.TodoItemRepository,
    @BackgroundDispatcher private val coroutineDispatcher: CoroutineDispatcher,
) : ConnectivityManager.NetworkCallback() {

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        CoroutineScope(coroutineDispatcher).launch {
            todoItemRepository.updateItems()
        }
    }

}