package ru.gribbirg.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.gribbirg.domain.model.todo.TodoImportance
import ru.gribbirg.domain.model.todo.TodoItem
import ru.gribbirg.utils.extensions.toLocalDate
import ru.gribbirg.utils.extensions.toLocalDateTime
import ru.gribbirg.utils.extensions.toTimestamp

/**
 * Item Dto
 */
@Serializable
data class TodoItemDto(
    val id: String,
    val text: String,
    val importance: String,
    val deadline: Long? = null,
    val done: Boolean,
    val color: String? = null,
    @SerialName("created_at") val createdAt: Long,
    @SerialName("changed_at") val changedAt: Long,
    @SerialName("last_updated_by") val updateBy: String,
) {
    fun toTodoItem() =
        TodoItem(
            id = id,
            text = text,
            importance = importance.dtoToImportance()!!,
            deadline = deadline?.toLocalDate(),
            completed = done,
            creationDate = createdAt.toLocalDateTime()!!,
            editDate = changedAt.toLocalDateTime()!!,
        )
}

fun TodoItem.toNetworkDto(deviceId: String) =
    TodoItemDto(
        id = id,
        text = text,
        importance = importance.toNetworkDto(),
        deadline = deadline?.toTimestamp(),
        done = completed,
        color = null,
        createdAt = creationDate.toTimestamp(),
        changedAt = editDate.toTimestamp(),
        updateBy = deviceId,
    )


private fun TodoImportance.toNetworkDto() = when (this) {
    TodoImportance.No -> "basic"
    TodoImportance.Low -> "low"
    TodoImportance.High -> "important"
}

private fun String.dtoToImportance() = when (this) {
    "basic" -> TodoImportance.No
    "low" -> TodoImportance.Low
    "important" -> TodoImportance.High
    else -> null
}