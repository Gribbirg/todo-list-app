package ru.gribbirg.todoapp.data.data

import java.time.LocalDate

data class TodoItem(
    val id: String = "",
    val text: String = "",
    val importance: TodoImportance = TodoImportance.NO,
    val deadline: LocalDate? = null,
    val completed: Boolean = false,
    val creationDate: LocalDate = LocalDate.now(),
    val editDate: LocalDate? = null
)
