package ru.gribbirg.todoapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

const val DB_NAME = "todo_list_database"

/**
 * Database to handle local items
 *
 * @see TodoDbEntity
 * @see TodoDao
 * @see DatabaseConverters
 */
@Database(
    entities = [TodoDbEntity::class],
    version = 9,
    exportSchema = false,
)
@TypeConverters(DatabaseConverters::class)
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