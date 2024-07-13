package ru.gribbirg.todoapp.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.gribbirg.data.di.DataDependencies
import ru.gribbirg.data.di.DataScope
import ru.gribbirg.edit.di.EditFeatureDependencies
import ru.gribbirg.list.di.ListFeatureDependencies
import ru.gribbirg.network.di.modules.ApiClientScope
import ru.gribbirg.todoapp.TodoApplication
import ru.gribbirg.todoapp.di.modules.DataModule
import ru.gribbirg.todoapp.di.modules.EditFeatureModule
import ru.gribbirg.todoapp.di.modules.ListFeatureModule
import javax.inject.Scope

@Scope
annotation class AppScope

@Component(
    modules = [DataModule::class, ListFeatureModule::class, EditFeatureModule::class]
)
@DataScope
@AppScope
@ApiClientScope
internal interface AppComponent : DataDependencies, ListFeatureDependencies,
    EditFeatureDependencies {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): AppComponent
    }

    fun inject(application: TodoApplication)

    fun listFeatureComponent(): ListFeatureComponent

    fun editFeatureComponent(): EditFeatureComponent
}