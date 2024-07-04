package ru.gribbirg.todoapp

import android.app.Application
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import ru.gribbirg.todoapp.data.datestore.DataStoreUtil
import ru.gribbirg.todoapp.data.db.TodoDao
import ru.gribbirg.todoapp.data.db.TodoDatabase
import ru.gribbirg.todoapp.data.repositories.TodoItemRepository
import ru.gribbirg.todoapp.data.repositories.TodoItemRepositoryImpl
import ru.gribbirg.todoapp.network.ApiClient
import ru.gribbirg.todoapp.utils.TodoListUpdateWorkManager
import java.util.concurrent.TimeUnit

class TodoApplication : Application() {
    private val apiClient: ApiClient by lazy {
        ApiClient(dataStore = DataStoreUtil(applicationContext, "internet_data"))
    }
    private val itemsDbDao: TodoDao by lazy {
        TodoDatabase.getInstance(
            applicationContext
        ).getTodoDao()
    }

    val todoItemRepository: TodoItemRepository by lazy {
        TodoItemRepositoryImpl(
            todoDao = itemsDbDao,
            apiClient = apiClient,
            context = applicationContext,
        )
    }

    override fun onCreate() {
        super.onCreate()
        scheduleDatabaseUpdate()
    }

    private fun scheduleDatabaseUpdate() {
        val updateDataWorkRequest =
            PeriodicWorkRequestBuilder<TodoListUpdateWorkManager>(15, TimeUnit.MINUTES)
                .build()

        WorkManager.getInstance(this).enqueue(updateDataWorkRequest)
    }
}