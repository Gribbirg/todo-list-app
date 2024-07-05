package ru.gribbirg.todoapp.data.logic

import ru.gribbirg.todoapp.data.data.TodoItem
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