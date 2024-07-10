package ru.gribbirg.todoapp.data.db

import androidx.room.Database
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
}