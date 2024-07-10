package ru.gribbirg.todoapp

import android.app.Application
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import ru.gribbirg.todoapp.data.db.ItemsLocalClient
import ru.gribbirg.todoapp.data.db.TodoDatabase
import ru.gribbirg.todoapp.data.keyvaluesaver.DataStoreSaver
import ru.gribbirg.todoapp.data.keyvaluesaver.KeyValueDataSaver
import ru.gribbirg.todoapp.data.logic.listMergerImpl
import ru.gribbirg.todoapp.data.network.ItemsApiClient
import ru.gribbirg.todoapp.data.network.ItemsApiClientImpl
import ru.gribbirg.todoapp.data.network.NetworkConstants
import ru.gribbirg.todoapp.data.repositories.items.TodoItemRepository
import ru.gribbirg.todoapp.data.repositories.items.TodoItemRepositoryImpl
import ru.gribbirg.todoapp.data.repositories.login.LoginRepository
import ru.gribbirg.todoapp.data.repositories.login.LoginRepositoryImpl
import ru.gribbirg.todoapp.data.systemdata.SystemDataProvider
import ru.gribbirg.todoapp.data.systemdata.SystemDataProviderImpl
import ru.gribbirg.todoapp.utils.TodoListUpdateNetworkCallback
import ru.gribbirg.todoapp.utils.TodoListUpdateWorkManager
import java.util.concurrent.TimeUnit

/**
 * Application class, handle main dependencies
 */
class TodoApplication : Application() {

    private val internetDataStore: KeyValueDataSaver by lazy {
        DataStoreSaver(applicationContext, NetworkConstants.KEY_VALUE_SAVER_NAME)
    }

    private val apiClient: ItemsApiClient by lazy {
        ItemsApiClientImpl(dataStore = internetDataStore)
    }
    private val localClient: ItemsLocalClient by lazy {
        TodoDatabase.getInstance(
            applicationContext
        ).getTodoDao()
    }
    private val systemDataProvider: SystemDataProvider by lazy {
        SystemDataProviderImpl(applicationContext)
    }

    val todoItemRepository: TodoItemRepository by lazy {
        TodoItemRepositoryImpl(
            localClient = localClient,
            apiClient = apiClient,
            systemDataProvider = systemDataProvider,
            loginRepository = loginRepository,
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