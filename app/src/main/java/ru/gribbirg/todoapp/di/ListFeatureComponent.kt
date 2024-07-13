package ru.gribbirg.todoapp.di

import dagger.Subcomponent
import ru.gribbirg.list.TodoItemsListViewModel
import ru.gribbirg.todoapp.di.modules.EditFeatureModule

@Subcomponent(modules = [EditFeatureModule::class])
interface ListFeatureComponent {
    fun listViewModel(): TodoItemsListViewModel
}