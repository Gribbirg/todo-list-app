package ru.gribbirg.domain.utils

import ru.gribbirg.domain.model.todo.TodoItem
import java.time.LocalDateTime

/**
 * Merge two lists: local and from server
 */
fun interface ItemsListsMerger {
    fun mergeLists(
        local: List<TodoItem>,
        internet: List<TodoItem>,
        lastUpdateTime: LocalDateTime
    ): List<TodoItem>
}