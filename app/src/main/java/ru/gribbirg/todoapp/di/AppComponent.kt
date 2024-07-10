package ru.gribbirg.todoapp.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.gribbirg.todoapp.TodoApplication

@Component(modules = [AppModule::class, DatabaseModule::class, ApiModule::class, CoroutineModule::class])
@ApiClientScope
@DatabaseScope
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): AppComponent
    }

    fun inject(application: TodoApplication)
}