package ru.gribbirg.todoapp.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.gribbirg.todoapp.data.db.DB_NAME
import ru.gribbirg.todoapp.data.db.ItemsLocalClient
import ru.gribbirg.todoapp.data.db.TodoDatabase
import javax.inject.Scope

@Scope
annotation class DatabaseScope

@Module
interface DatabaseModule {

    companion object {
        @Provides
        @DatabaseScope
        fun localDb(context: Context): TodoDatabase =
            Room
                .databaseBuilder(context, TodoDatabase::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()

        @Provides
        @DatabaseScope
        fun localClient(db: TodoDatabase): ItemsLocalClient =
            db.getTodoDao()
    }
}