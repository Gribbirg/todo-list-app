package ru.gribbirg.domain.model.todo

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Main data class of item
 *
 */
data class TodoItem(
    val id: String = "",
    val text: String = "",
    val importance: TodoImportance = TodoImportance.No,
    val deadline: LocalDate? = null,
    val completed: Boolean = false,
    val creationDate: LocalDateTime = LocalDateTime.now(ZoneId.of("UTC")),
    val editDate: LocalDateTime = creationDate,
)