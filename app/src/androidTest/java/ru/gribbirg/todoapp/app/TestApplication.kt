package ru.gribbirg.todoapp.app

import android.app.Application
import ru.gribbirg.todoapp.di.DaggerTestAppComponent
import ru.gribbirg.todoapp.di.TestAppComponent

class TestApplication : Application() {
    internal val testAppComponent: TestAppComponent by lazy {
        DaggerTestAppComponent
            .factory()
            .create(applicationContext)
    }
}