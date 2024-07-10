package ru.gribbirg.todoapp

import android.app.Application
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import ru.gribbirg.todoapp.data.repositories.items.TodoItemRepository
import ru.gribbirg.todoapp.data.repositories.login.LoginRepository
import ru.gribbirg.todoapp.di.DaggerAppComponent
import ru.gribbirg.todoapp.utils.TodoListUpdateNetworkCallback
import ru.gribbirg.todoapp.utils.TodoListUpdateWorkManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Application class, handle main dependencies
 */
class TodoApplication : Application() {

    @Inject
    lateinit var todoItemRepository: TodoItemRepository

    @Inject
    lateinit var loginRepository: LoginRepository

    val appComponent by lazy {
        DaggerAppComponent
            .factory()
            .create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()

        appComponent.inject(this)

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