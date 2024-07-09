package ru.gribbirg.todoapp.data.db

import androidx.room.TypeConverter
import ru.gribbirg.todoapp.data.data.TodoImportance
import ru.gribbirg.todoapp.utils.toLocalDate
import ru.gribbirg.todoapp.utils.toLocalDateTime
import ru.gribbirg.todoapp.utils.toTimestamp
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Type converters for db
 *
 * @see TodoDatabase
 */
class DatabaseConverters {
    @TypeConverter
    fun localDateToDatestamp(date: LocalDate?): Long? =
        date?.toTimestamp()

    @TypeConverter
    fun datestampToLocalDate(datestamp: Long?): LocalDate? =
        datestamp?.toLocalDate()

    @TypeConverter
    fun localDateTimeToDatestamp(date: LocalDateTime?) =
        date?.toTimestamp()

    @TypeConverter
    fun datestampToLocalDateTime(datestamp: Long?): LocalDateTime? =
        datestamp?.toLocalDateTime()

    @TypeConverter
    fun todoImportanceToString(importance: TodoImportance) = importance.name

    @TypeConverter
    fun stringToTodoImportance(importanceName: String) = TodoImportance.valueOf(importanceName)
}