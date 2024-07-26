package ru.gribbirg.todoapp.di.modules

import dagger.Binds
import dagger.Module
import ru.gribbirg.todoapp.di.AppComponent
import ru.gribbirg.todoapp.di.TestAppComponent

@Module
internal interface TestAppModule {
    @Binds
    fun appComponent(testAppComponent: TestAppComponent): AppComponent
}