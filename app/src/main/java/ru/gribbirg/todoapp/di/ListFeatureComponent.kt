package ru.gribbirg.todoapp.di

import dagger.Subcomponent
import ru.gribbirg.list.TodoItemsListViewModel
import ru.gribbirg.todoapp.di.modules.ListFeatureModule

@Subcomponent(modules = [ListFeatureModule::class])
interface ListFeatureComponent {
    fun listViewModel(): TodoItemsListViewModel
}