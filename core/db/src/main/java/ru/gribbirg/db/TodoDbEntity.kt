package ru.gribbirg.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.gribbirg.domain.model.todo.TodoImportance
import ru.gribbirg.domain.model.todo.TodoItem
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

@Entity(
    tableName = DB_NAME,
)
data class TodoDbEntity(
    @PrimaryKey val id: String = "",
    val text: String = "",
    val importance: TodoImportance = TodoImportance.No,
    val deadline: LocalDate? = null,
    val completed: Boolean = false,
    @ColumnInfo(name = "creation_date")
    val creationDate: LocalDateTime = LocalDateTime.now(ZoneId.of("UTC")),
    @ColumnInfo(name = "edit_date")
    val editDate: LocalDateTime = creationDate,
) {
    fun toTodoItem(): TodoItem =
        TodoItem(
            id = id,
            text = text,
            importance = importance,
            deadline = deadline,
            completed = completed,
            creationDate = creationDate,
            editDate = editDate,
        )
}

fun TodoItem.toLocalDbItem(): TodoDbEntity =
    TodoDbEntity(
        id = id,
        text = text,
        importance = importance,
        deadline = deadline,
        completed = completed,
        creationDate = creationDate,
        editDate = editDate,
    )