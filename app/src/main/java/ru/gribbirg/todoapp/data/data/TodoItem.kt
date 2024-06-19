package ru.gribbirg.todoapp.data.data

import java.time.LocalDate
import java.util.Date

data class TodoItem(
    val id: String = "",
    val text: String = "",
    val importance: TodoImportance = TodoImportance.MEDIUM,
    val deadline: LocalDate? = null,
    val completed: Boolean = false,
    val creationDate: LocalDate = LocalDate.now(),
    val editDate: LocalDate? = null
)


enum class TodoImportance {
    LOW,
    MEDIUM,
    HIGH
}
