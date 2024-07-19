package ru.gribbirg.todoapp.di.modules

import dagger.Module
import dagger.Provides
import ru.gribbirg.about.di.AboutFeatureFactory
import ru.gribbirg.todoapp.di.AppComponent

@Module
internal interface AboutFeatureModule {
    companion object {
        @Provides
        fun aboutFeatureFactory(depsImpl: AppComponent): AboutFeatureFactory =
            AboutFeatureFactory(depsImpl)

        @Provides
        fun aboutFeatureViewModel(factory: AboutFeatureFactory) =
            factory.createViewModel()
    }
}