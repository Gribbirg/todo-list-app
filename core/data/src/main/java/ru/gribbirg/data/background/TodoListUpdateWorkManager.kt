package ru.gribbirg.data.background

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.flow.first
import ru.gribbirg.domain.model.NetworkState
import ru.gribbirg.domain.repositories.TodoItemRepository

/**
 * Work manager for regular background data sync
 */
class TodoListUpdateWorkManager(
    context: Context,
    workerParameters: WorkerParameters,
    private val repository: TodoItemRepository,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
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