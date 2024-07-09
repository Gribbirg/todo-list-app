package ru.gribbirg.todoapp

import android.app.Application
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import ru.gribbirg.todoapp.data.db.TodoDao
import ru.gribbirg.todoapp.data.db.TodoDatabase
import ru.gribbirg.todoapp.data.keyvaluesaver.DataStoreSaver
import ru.gribbirg.todoapp.data.logic.listMergerImpl
import ru.gribbirg.todoapp.data.network.ItemsApiClientImpl
import ru.gribbirg.todoapp.data.repositories.items.TodoItemRepository
import ru.gribbirg.todoapp.data.repositories.items.TodoItemRepositoryImpl
import ru.gribbirg.todoapp.data.repositories.login.LoginRepository
import ru.gribbirg.todoapp.data.repositories.login.LoginRepositoryImpl
import ru.gribbirg.todoapp.utils.TodoListUpdateNetworkCallback
import ru.gribbirg.todoapp.utils.TodoListUpdateWorkManager
import java.util.concurrent.TimeUnit

/**
 * Application class, handle main dependencies
 */
class TodoApplication : Application() {

    private val internetDataStore by lazy {
        DataStoreSaver(applicationContext, "internet_data")
    }

    private val apiClientImpl: ItemsApiClientImpl by lazy {
        ItemsApiClientImpl(dataStore = internetDataStore)
    }
    private val itemsDbDao: TodoDao by lazy {
        TodoDatabase.getInstance(
            applicationContext
        ).getTodoDao()
    }

    val todoItemRepository: TodoItemRepository by lazy {
        TodoItemRepositoryImpl(
            todoDao = itemsDbDao,
            apiClientImpl = apiClientImpl,
            context = applicationContext,
            internetDataStore = internetDataStore,
            listsMerger = listMergerImpl,
        )
    }

    val loginRepository: LoginRepository by lazy {
        LoginRepositoryImpl(
            internetDataStore = internetDataStore,
        )
    }

    override fun onCreate() {
        super.onCreate()
        scheduleDatabaseUpdate()
        registerConnectivityManager()
    }

    private fun scheduleDatabaseUpdate() {
        val updateDataWorkRequest =
            PeriodicWorkRequestBuilder<TodoListUpdateWorkManager>(8, TimeUnit.HOURS)
                .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            TodoListUpdateWorkManager.WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            updateDataWorkRequest,
        )
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