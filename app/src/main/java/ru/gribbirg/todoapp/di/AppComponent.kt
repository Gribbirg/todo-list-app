package ru.gribbirg.todoapp.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.gribbirg.todoapp.TodoApplication
import ru.gribbirg.todoapp.di.modules.ApiClientScope
import ru.gribbirg.todoapp.di.modules.ApiModule
import ru.gribbirg.todoapp.di.modules.AppModule
import ru.gribbirg.todoapp.di.modules.CoroutineModule
import ru.gribbirg.todoapp.di.modules.DatabaseModule
import ru.gribbirg.todoapp.di.modules.DatabaseScope
import ru.gribbirg.todoapp.di.modules.SystemDataModule
import ru.gribbirg.todoapp.di.modules.SystemDataScope

@Component(
    modules = [
        AppModule::class,
        DatabaseModule::class,
        ApiModule::class,
        CoroutineModule::class,
        SystemDataModule::class,
    ]
)
@ApiClientScope
@DatabaseScope
@SystemDataScope
interface AppComponent {

    fun inject(application: TodoApplication)

    fun listScreenComponent(): ListScreenComponent

    fun editScreenComponent(): EditScreenComponent

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): AppComponent
    }
}