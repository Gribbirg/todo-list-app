package ru.gribbirg.data.di

import android.net.ConnectivityManager
import androidx.work.WorkerFactory
import ru.gribbirg.domain.repositories.LoginRepository
import ru.gribbirg.domain.repositories.TodoItemRepository

class DataFactory(
    deps: DataDependencies,
) {
    private val component: DataComponent = DaggerDataComponent.factory().create(deps)

    fun createItemsRepository(): TodoItemRepository = component.itemsRepository

    fun createLoginRepository(): LoginRepository = component.loginRepository

    fun createWorkerFactory(): WorkerFactory = component.workerFactory

    fun createNetworkCallback(): ConnectivityManager.NetworkCallback = component.networkCallback
}