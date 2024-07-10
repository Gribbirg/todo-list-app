package ru.gribbirg.todoapp.di

import dagger.Subcomponent
import ru.gribbirg.todoapp.ui.screens.edititem.EditItemViewModel
import javax.inject.Scope

@Scope
annotation class EditScreenScope

@Subcomponent
@EditScreenScope
interface EditScreenComponent {
    fun viewModelFactory(): EditItemViewModel.Factory
}