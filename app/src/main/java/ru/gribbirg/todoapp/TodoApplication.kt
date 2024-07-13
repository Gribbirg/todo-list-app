package ru.gribbirg.todoapp

import android.app.Application
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import ru.gribbirg.data.background.TodoListUpdateWorkManager
import ru.gribbirg.todoapp.di.DaggerAppComponent
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Application class, handle main dependencies
 */
class TodoApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: WorkerFactory

    @Inject
    lateinit var networkCallback: ConnectivityManager.NetworkCallback

    internal val appComponent by lazy {
        DaggerAppComponent
            .factory()
            .create(applicationContext)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setWorkerFactory(workerFactory).build()

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
        val connectivityManager =
            getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }
}