package ru.gribbirg.data.di

import dagger.Component
import ru.gribbirg.data.LoginRepositoryImpl
import ru.gribbirg.data.TodoItemRepositoryImpl
import ru.gribbirg.data.background.TodoListUpdateNetworkCallback
import ru.gribbirg.data.background.TodoWorkerFactory
import ru.gribbirg.data.di.modules.CoroutinesModule
import ru.gribbirg.data.di.modules.DatabaseModule
import ru.gribbirg.data.di.modules.NetworkModule
import ru.gribbirg.data.di.modules.RepositoriesModule
import ru.gribbirg.data.di.modules.UtilsModule
import ru.gribbirg.db.di.DatabaseDependencies
import ru.gribbirg.db.di.DatabaseScope
import ru.gribbirg.network.di.NetworkDependencies
import ru.gribbirg.network.di.modules.ApiClientScope
import ru.gribbirg.utils.di.UtilsDependencies
import javax.inject.Scope

@Scope
annotation class DataScope

@Component(
    dependencies = [DataDependencies::class],
    modules = [
        CoroutinesModule::class,
        NetworkModule::class,
        UtilsModule::class,
        DatabaseModule::class,
        RepositoriesModule::class,
    ],
)
@DataScope
@DatabaseScope
@ApiClientScope
internal interface DataComponent : NetworkDependencies, DatabaseDependencies, UtilsDependencies {
    @Component.Factory
    interface Factory {
        fun create(deps: DataDependencies): DataComponent
    }

    val itemsRepository: TodoItemRepositoryImpl

    val loginRepository: LoginRepositoryImpl

    val workerFactory: TodoWorkerFactory

    val networkCallback: TodoListUpdateNetworkCallback
}