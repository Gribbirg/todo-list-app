package ru.gribbirg.db

import androidx.room.TypeConverter
import ru.gribbirg.domain.model.TodoImportance
import ru.gribbirg.utils.extensions.toLocalDate
import ru.gribbirg.utils.extensions.toLocalDateTime
import ru.gribbirg.utils.extensions.toTimestamp
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Type converters for db
 *
 * @see TodoDatabase
 */
internal class DatabaseConverters {
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
    fun todoImportanceToString(importance: TodoImportance) =
        importance.name

    @TypeConverter
    fun stringToTodoImportance(importanceName: String) =
        TodoImportance.valueOf(importanceName)
}