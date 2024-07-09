package ru.gribbirg.todoapp.utils

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.flow.first
import ru.gribbirg.todoapp.TodoApplication
import ru.gribbirg.todoapp.data.repositories.items.NetworkState

/**
 * Work manager for regular background data sync
 */
class TodoListUpdateWorkManager(
    context: Context,
    workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val repository = (applicationContext as TodoApplication).todoItemRepository
        repository.updateItems()
        return when (repository.getNetworkStateFlow().first()) {
            is NetworkState.Success -> Result.success()
            is NetworkState.Error.NetworkError -> Result.retry()
            is NetworkState.Error -> Result.failure()
            else -> Result.success()
        }
    }

    companion object {
        const val WORK_NAME = "update_list"
    }
}