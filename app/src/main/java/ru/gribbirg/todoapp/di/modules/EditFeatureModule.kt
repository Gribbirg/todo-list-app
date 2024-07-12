package ru.gribbirg.todoapp.di.modules

import dagger.Module
import dagger.Provides
import ru.gribbirg.edit.di.EditFeatureFactory
import ru.gribbirg.todoapp.di.AppComponent

@Module
internal interface EditFeatureModule {
    companion object {
        @Provides
        fun editFeatureFactory(depsImpl: AppComponent): EditFeatureFactory =
            EditFeatureFactory(depsImpl)
    }
}