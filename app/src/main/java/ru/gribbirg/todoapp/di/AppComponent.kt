package ru.gribbirg.todoapp.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.gribbirg.about.di.AboutFeatureDependencies
import ru.gribbirg.data.di.DataDependencies
import ru.gribbirg.data.di.DataScope
import ru.gribbirg.domain.utils.SettingsHandler
import ru.gribbirg.edit.di.EditFeatureDependencies
import ru.gribbirg.list.di.ListFeatureDependencies
import ru.gribbirg.network.di.modules.ApiClientScope
import ru.gribbirg.settings.di.SettingsFeatureDependencies
import ru.gribbirg.todoapp.TodoApplication
import ru.gribbirg.todoapp.di.modules.AboutFeatureModule
import ru.gribbirg.todoapp.di.modules.DataModule
import ru.gribbirg.todoapp.di.modules.EditFeatureModule
import ru.gribbirg.todoapp.di.modules.ListFeatureModule
import ru.gribbirg.todoapp.di.modules.UtilsModule
import ru.gribbirg.utils.di.UtilsDependencies
import ru.gribbirg.utils.di.modules.AppSettingsScope
import javax.inject.Scope

@Scope
annotation class AppScope

@Component(
    modules = [
        DataModule::class,
        ListFeatureModule::class,
        EditFeatureModule::class,
        UtilsModule::class,
        AboutFeatureModule::class,
    ]
)
@DataScope
@AppScope
@ApiClientScope
@AppSettingsScope
internal interface AppComponent :
    DataDependencies,
    UtilsDependencies,
    ListFeatureDependencies,
    EditFeatureDependencies,
    SettingsFeatureDependencies,
    AboutFeatureDependencies {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): AppComponent
    }

    fun inject(application: TodoApplication)

    fun listFeatureComponent(): ListFeatureComponent

    fun editFeatureComponent(): EditFeatureComponent

    fun settingsFeatureComponent(): SettingsFeatureComponent

    fun aboutFeatureComponent(): AboutFeatureComponent

    fun settingsHandler(): SettingsHandler
}