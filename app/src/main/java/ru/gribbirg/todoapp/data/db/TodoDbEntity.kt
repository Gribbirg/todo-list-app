package ru.gribbirg.todoapp.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.gribbirg.todoapp.data.constants.DB_NAME
import ru.gribbirg.todoapp.data.data.TodoImportance
import ru.gribbirg.todoapp.data.data.TodoItem
import java.time.LocalDate

@Entity(
    tableName = DB_NAME,
)
data class TodoDbEntity(
    @PrimaryKey val id: String = "",
    val text: String = "",
    val importance: TodoImportance = TodoImportance.NO,
    val deadline: LocalDate? = null,
    val completed: Boolean = false,
    @ColumnInfo(name = "creation_date") val creationDate: LocalDate = LocalDate.now(),
    @ColumnInfo(name = "edit_date") val editDate: LocalDate? = null
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