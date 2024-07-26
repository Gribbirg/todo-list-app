package ru.gribbirg.todoapp.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.gribbirg.data.di.DataScope
import ru.gribbirg.domain.repositories.LoginRepository
import ru.gribbirg.network.di.modules.ApiClientScope
import ru.gribbirg.todoapp.di.modules.AboutFeatureModule
import ru.gribbirg.todoapp.di.modules.DataModule
import ru.gribbirg.todoapp.di.modules.EditFeatureModule
import ru.gribbirg.todoapp.di.modules.ListFeatureModule
import ru.gribbirg.todoapp.di.modules.TestAppModule
import ru.gribbirg.todoapp.di.modules.TestDataModule
import ru.gribbirg.todoapp.di.modules.TestNetworkModule
import ru.gribbirg.todoapp.di.modules.UtilsModule
import ru.gribbirg.utils.di.modules.AppSettingsScope


@Component(
    modules = [
        DataModule::class,
        ListFeatureModule::class,
        EditFeatureModule::class,
        UtilsModule::class,
        AboutFeatureModule::class,
        TestNetworkModule::class,
        TestAppModule::class,
        TestDataModule::class,
    ]
)
@DataScope
@AppScope
@ApiClientScope
@AppSettingsScope
internal interface TestAppComponent : AppComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): TestAppComponent
    }

    val loginRepo: LoginRepository
}