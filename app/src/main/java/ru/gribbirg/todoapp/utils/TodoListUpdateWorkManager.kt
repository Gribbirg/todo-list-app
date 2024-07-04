package ru.gribbirg.todoapp.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.gribbirg.todoapp.TodoApplication
import kotlin.coroutines.CoroutineContext

class TodoListUpdateWorkManager(
    context: Context,
    workerParameters: WorkerParameters,
) : Worker(context, workerParameters) {

    //private val repository: TodoItemRepository = TodoItemRepositoryImpl(
    //    todoDao = TodoDatabase.getInstance(context).getTodoDao(),
    //    apiClient = ApiClient(DataStoreUtil(context, "internet_data")),
    //    context = context
    //)

    private val coroutineContext: CoroutineContext = Dispatchers.IO

    override fun doWork(): Result {
        val repository = (applicationContext as TodoApplication).todoItemRepository
        CoroutineScope(coroutineContext).launch {
            repository.updateItems()
        }
        return Result.success()
    }
}