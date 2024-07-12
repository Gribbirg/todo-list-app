package ru.gribbirg.data.background

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import ru.gribbirg.domain.repositories.TodoItemRepository
import javax.inject.Inject

internal class TodoWorkerFactory @Inject constructor(
    private val todoItemRepository: TodoItemRepository
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? = when (workerClassName) {
        TodoListUpdateWorkManager::class.java.name -> TodoListUpdateWorkManager(
            appContext,
            workerParameters,
            todoItemRepository
        )

        else -> null
    }
}