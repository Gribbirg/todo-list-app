package ru.gribbirg.todoapp

import android.app.Application
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import ru.gribbirg.todoapp.data.datestore.DataStoreUtil
import ru.gribbirg.todoapp.data.db.TodoDao
import ru.gribbirg.todoapp.data.db.TodoDatabase
import ru.gribbirg.todoapp.data.repositories.TodoItemRepository
import ru.gribbirg.todoapp.data.repositories.TodoItemRepositoryImpl
import ru.gribbirg.todoapp.network.ApiClient
import ru.gribbirg.todoapp.utils.TodoListUpdateNetworkCallback
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
        registerConnectivityManager()
    }

    private fun scheduleDatabaseUpdate() {
        val updateDataWorkRequest =
            PeriodicWorkRequestBuilder<TodoListUpdateWorkManager>(15, TimeUnit.MINUTES)
                .build()

        WorkManager.getInstance(this).enqueue(updateDataWorkRequest)
    }

    private fun registerConnectivityManager() {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
        val networkCallback = TodoListUpdateNetworkCallback(todoItemRepository)
        val connectivityManager =
            getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }
}