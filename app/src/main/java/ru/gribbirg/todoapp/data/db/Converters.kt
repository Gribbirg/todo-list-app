package ru.gribbirg.todoapp.data.db

import androidx.room.TypeConverter
import ru.gribbirg.todoapp.data.data.TodoImportance
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class Converters {
    @TypeConverter
    fun localDateToDatestamp(date: LocalDate?): Long? =
        date?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()

    @TypeConverter
    fun datestampToLocalDate(datestamp: Long?): LocalDate? =
        datestamp?.let { Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate() }

    @TypeConverter
    fun todoImportanceToString(importance: TodoImportance) = importance.name

    @TypeConverter
    fun stringToTodoImportance(importanceName: String) = TodoImportance.valueOf(importanceName)
}