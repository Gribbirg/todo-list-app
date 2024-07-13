package ru.gribbirg.db.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.gribbirg.db.DB_NAME
import ru.gribbirg.db.TodoDao
import ru.gribbirg.db.TodoDatabase
import ru.gribbirg.db.di.DatabaseScope

@Module
internal interface RoomModule {
    companion object {
        @DatabaseScope
        @Provides
        fun database(context: Context): TodoDatabase = Room
            .databaseBuilder(context, TodoDatabase::class.java, DB_NAME)
            .fallbackToDestructiveMigration()
            .build()

        @Provides
        @DatabaseScope
        fun dbDao(db: TodoDatabase): TodoDao = db.getTodoDao()
    }
}