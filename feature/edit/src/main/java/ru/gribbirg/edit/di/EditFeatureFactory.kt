package ru.gribbirg.edit.di

import ru.gribbirg.edit.EditItemViewModel

class EditFeatureFactory(deps: EditFeatureDependencies) {
    private val component: EditFeatureComponent = DaggerEditFeatureComponent.factory().create(deps)

    fun createViewModelFactory(): EditItemViewModel.Factory =
        component.viewModelFactory
}