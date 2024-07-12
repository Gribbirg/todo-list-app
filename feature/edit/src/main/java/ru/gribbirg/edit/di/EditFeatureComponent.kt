package ru.gribbirg.edit.di

import dagger.Component
import ru.gribbirg.edit.EditItemViewModel
import ru.gribbirg.edit.di.models.RepositoryModule

@Component(dependencies = [EditFeatureDependencies::class], modules = [RepositoryModule::class])
internal interface EditFeatureComponent {
    @Component.Factory
    interface Factory {
        fun create(deps: EditFeatureDependencies): EditFeatureComponent
    }

    val viewModelFactory: EditItemViewModel.Factory
}