package ru.gribbirg.settings.di.modules

import dagger.Module
import dagger.Provides
import ru.gribbirg.data.di.DataFactory
import ru.gribbirg.domain.repositories.LoginRepository

@Module
internal interface RepositoryModule {
    companion object {
        @Provides
        fun loginRepository(factory: DataFactory): LoginRepository = factory.createLoginRepository()
    }
}