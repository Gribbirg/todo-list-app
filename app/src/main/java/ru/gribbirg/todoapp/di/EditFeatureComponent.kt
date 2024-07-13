package ru.gribbirg.todoapp.di

import dagger.Subcomponent
import ru.gribbirg.edit.di.EditFeatureFactory
import ru.gribbirg.todoapp.di.modules.EditFeatureModule

@Subcomponent(modules = [EditFeatureModule::class])
interface EditFeatureComponent {
    fun editFeatureFactory(): EditFeatureFactory
}