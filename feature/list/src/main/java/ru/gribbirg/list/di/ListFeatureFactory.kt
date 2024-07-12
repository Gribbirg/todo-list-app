package ru.gribbirg.list.di

import ru.gribbirg.list.TodoItemsListViewModel

class ListFeatureFactory(deps: ListFeatureDependencies) {
    private val component: ListFeatureComponent = DaggerListFeatureComponent.factory().create(deps)

    fun createViewModel(): TodoItemsListViewModel = component.viewModel
}