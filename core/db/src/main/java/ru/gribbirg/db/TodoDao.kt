package ru.gribbirg.db

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
internal interface TodoDao : ItemsLocalClient {
    @Query("SELECT * FROM $DB_NAME")
    override fun getAll(): List<TodoDbEntity>

    @Query("SELECT * FROM $DB_NAME")
    override fun getItemsFlow(): Flow<List<TodoDbEntity>>

    @Query("SELECT * FROM $DB_NAME WHERE id = :id LIMIT 1")
    override suspend fun getItem(id: String): TodoDbEntity?

    @Update
    override suspend fun updateItem(item: TodoDbEntity)

    @Insert
    override suspend fun addItem(item: TodoDbEntity)

    @Query("DELETE FROM $DB_NAME WHERE id = :id")
    override suspend fun deleteItemById(id: String)

    @Insert
    override suspend fun addAll(items: List<TodoDbEntity>)

    @Query("DELETE FROM $DB_NAME")
    override suspend fun deleteAll()

    @Transaction
    override suspend fun refreshItems(items: List<TodoDbEntity>) {
        deleteAll()
        addAll(items)
    }
}