package ru.gribbirg.todoapp.data.data

import ru.gribbirg.todoapp.R
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


enum class TodoImportance(val resourceId: Int, val logoId: Int? = null, val colorId: Int? = null) {
    NO(
        resourceId = R.string.importence_no
    ),
    LOW(
        resourceId = R.string.importance_low,
        logoId = R.drawable.baseline_south_24
    ),
    HIGH(
        resourceId = R.string.importance_high,
        logoId = R.drawable.baseline_priority_high_24,
        colorId = R.color.red
    )
}
