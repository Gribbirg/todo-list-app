package ru.gribbirg.todoapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Dao for database
 *
 * @see TodoDatabase
 */
@Dao
interface TodoDao {
    @Query("SELECT * FROM $DB_NAME")
    fun getAll(): List<TodoDbEntity>

    @Query("SELECT * FROM $DB_NAME")
    fun getItemsFlow(): Flow<List<TodoDbEntity>>

    @Query("SELECT * FROM $DB_NAME WHERE id = :id LIMIT 1")
    suspend fun getItem(id: String): TodoDbEntity?

    @Update
    suspend fun updateItem(item: TodoDbEntity)

    @Insert
    suspend fun addItem(item: TodoDbEntity)

    @Query("DELETE FROM $DB_NAME WHERE id = :id")
    suspend fun deleteItemById(id: String)

    @Insert
    suspend fun addAll(items: List<TodoDbEntity>)

    @Query("DELETE FROM $DB_NAME")
    suspend fun deleteAll()

    @Transaction
    suspend fun refreshItems(items: List<TodoDbEntity>) {
        deleteAll()
        addAll(items)
    }
}