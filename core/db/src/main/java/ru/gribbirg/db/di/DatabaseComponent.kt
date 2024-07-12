package ru.gribbirg.db.di

import dagger.Component
import ru.gribbirg.db.TodoDao
import ru.gribbirg.db.di.modules.RoomModule

@Component(dependencies = [DatabaseDependencies::class], modules = [RoomModule::class])
internal interface DatabaseComponent {
    @Component.Factory
    interface Factory {
        fun create(deps: DatabaseDependencies): DatabaseComponent
    }

    val dao: TodoDao
}