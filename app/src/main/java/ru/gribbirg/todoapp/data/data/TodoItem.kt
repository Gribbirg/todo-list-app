package ru.gribbirg.todoapp.data.data

import java.time.LocalDate
import java.time.LocalDateTime

data class TodoItem(
    val id: String = "",
    val text: String = "",
    val importance: TodoImportance = TodoImportance.NO,
    val deadline: LocalDate? = null,
    val completed: Boolean = false,
    val creationDate: LocalDateTime = LocalDateTime.now(),
    val editDate: LocalDateTime = creationDate,
) {
    companion object {
        val COMPARATOR_FOR_UI =
            compareBy<TodoItem>({ it.importance }, { it.deadline }, { it.editDate })
    }
}