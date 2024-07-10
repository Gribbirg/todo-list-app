package ru.gribbirg.todoapp.di

import dagger.Subcomponent
import ru.gribbirg.todoapp.ui.screens.todoitemslist.TodoItemsListViewModel
import javax.inject.Scope

@Scope
annotation class ListScreenScope

@Subcomponent
@ListScreenScope
interface ListScreenComponent {
    fun viewModel(): TodoItemsListViewModel
}