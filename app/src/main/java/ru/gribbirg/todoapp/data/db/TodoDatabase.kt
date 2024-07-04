package ru.gribbirg.todoapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.gribbirg.todoapp.data.constants.DB_NAME

@Database(
    entities = [TodoDbEntity::class],
    version = 2,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun getTodoDao(): TodoDao

    companion object {
        private var instance: TodoDatabase? = null

        fun getInstance(context: Context): TodoDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): TodoDatabase =
            Room
                .databaseBuilder(context, TodoDatabase::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }
}