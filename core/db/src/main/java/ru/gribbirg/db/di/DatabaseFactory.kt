package ru.gribbirg.db.di

import ru.gribbirg.db.ItemsLocalClient

class DatabaseFactory(
    deps: DatabaseDependencies
) {
    private val component: DatabaseComponent = DaggerDatabaseComponent.factory().create(deps)

    fun createLocalClient(): ItemsLocalClient = component.dao
}