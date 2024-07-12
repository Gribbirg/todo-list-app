package ru.gribbirg.todoapp.di.modules

import android.net.ConnectivityManager
import androidx.work.WorkerFactory
import dagger.Module
import dagger.Provides
import ru.gribbirg.data.di.DataFactory
import ru.gribbirg.todoapp.di.AppComponent
import ru.gribbirg.todoapp.di.AppScope

@Module
internal interface DataModule {
    companion object {
        @AppScope
        @Provides
        fun dataFactory(depsImpl: AppComponent): DataFactory = DataFactory(depsImpl)

        @Provides
        fun networkCallback(factory: DataFactory): ConnectivityManager.NetworkCallback =
            factory.createNetworkCallback()

        @Provides
        fun workerFactory(factory: DataFactory): WorkerFactory =
            factory.createWorkerFactory()
    }
}