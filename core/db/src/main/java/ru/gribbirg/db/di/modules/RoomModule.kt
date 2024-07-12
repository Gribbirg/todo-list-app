package ru.gribbirg.db.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.gribbirg.db.DB_NAME
import ru.gribbirg.db.TodoDao
import ru.gribbirg.db.TodoDatabase

@Module
internal interface RoomModule {
    companion object {
        @Provides
        fun database(context: Context): TodoDatabase = Room
            .databaseBuilder(context, TodoDatabase::class.java, DB_NAME)
            .fallbackToDestructiveMigration()
            .build()

        @Provides
        fun dbDao(db: TodoDatabase): TodoDao = db.getTodoDao()
    }
}