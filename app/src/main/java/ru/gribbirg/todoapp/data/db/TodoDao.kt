package ru.gribbirg.todoapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.gribbirg.todoapp.data.constants.DB_NAME

@Dao
interface TodoDao {
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
}