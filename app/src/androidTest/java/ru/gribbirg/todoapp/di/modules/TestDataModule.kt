package ru.gribbirg.todoapp.di.modules

import dagger.Module
import dagger.Provides
import ru.gribbirg.data.di.DataFactory
import ru.gribbirg.data.di.DataScope
import ru.gribbirg.domain.repositories.LoginRepository

@Module
interface TestDataModule {
    companion object {
        @Provides
        @DataScope
        fun loginRepo(factory: DataFactory): LoginRepository = factory.createLoginRepository()
    }
}