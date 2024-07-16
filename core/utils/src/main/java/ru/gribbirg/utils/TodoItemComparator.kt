package ru.gribbirg.utils

import ru.gribbirg.domain.model.todo.TodoItem
import ru.gribbirg.utils.extensions.toTimestamp

val todoItemComparatorForUi: Comparator<TodoItem> =
    compareBy(
        { -it.importance.power },
        { it.deadline?.toTimestamp()?.unaryMinus() ?: Long.MAX_VALUE },
        { -(it.creationDate.toTimestamp()) },
    )