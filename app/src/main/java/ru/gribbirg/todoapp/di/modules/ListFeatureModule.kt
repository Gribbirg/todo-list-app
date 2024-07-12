package ru.gribbirg.todoapp.di.modules

import dagger.Module
import dagger.Provides
import ru.gribbirg.list.di.ListFeatureFactory
import ru.gribbirg.todoapp.di.AppComponent

@Module
internal interface ListFeatureModule {
    companion object {
        @Provides
        fun listFeatureFactory(depsImpl: AppComponent): ListFeatureFactory =
            ListFeatureFactory(depsImpl)

        @Provides
        fun listFeatureViewModel(factory: ListFeatureFactory) =
            factory.createViewModel()
    }
}