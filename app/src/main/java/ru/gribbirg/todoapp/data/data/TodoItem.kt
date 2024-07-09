package ru.gribbirg.todoapp.data.data

import ru.gribbirg.todoapp.utils.toTimestamp
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
) {
    companion object {
        val COMPARATOR_FOR_UI =
            compareBy<TodoItem>(
                { -it.importance.power },
                { it.deadline?.toTimestamp()?.unaryMinus() ?: Long.MAX_VALUE },
                { -(it.creationDate.toTimestamp()) })
    }
}