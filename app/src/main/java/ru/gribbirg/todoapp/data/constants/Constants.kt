package ru.gribbirg.todoapp.data.constants

import ru.gribbirg.todoapp.data.data.TodoImportance
import ru.gribbirg.todoapp.data.data.TodoItem
import java.time.LocalDate

const val DB_NAME = "todo_list_database"

val defaultTodoItems: List<TodoItem>
    get() {
        return List(30) {
            TodoItem(
                id = it.toString(),
                text = "Дело $it ".repeat(it * 5 % 15 + 1),
                importance = TodoImportance.entries[it % 3],
                deadline = if (it % 3 == 0)
                    null
                else LocalDate.now().plusDays((-100L..100L).random()),
                completed = false,
                creationDate = LocalDate.now().minusDays((-0L..100L).random()),
                editDate = if (it % 5 == 0)
                    null
                else LocalDate.now(),
            )
        }
    }