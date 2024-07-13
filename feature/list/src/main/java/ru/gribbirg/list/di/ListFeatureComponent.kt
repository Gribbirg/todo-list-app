package ru.gribbirg.list.di

import dagger.Component
import ru.gribbirg.list.TodoItemsListViewModel
import ru.gribbirg.list.di.modules.RepositoryModule
import ru.gribbirg.list.di.modules.UtilsModule
import ru.gribbirg.utils.di.UtilsDependencies

@Component(
    dependencies = [ListFeatureDependencies::class],
    modules = [RepositoryModule::class, UtilsModule::class]
)
internal interface ListFeatureComponent : UtilsDependencies {
    @Component.Factory
    interface Factory {
        fun create(deps: ListFeatureDependencies): ListFeatureComponent
    }

    val viewModel: TodoItemsListViewModel
}