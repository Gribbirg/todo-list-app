package ru.gribbirg.domain.utils

import java.time.LocalDateTime

/**
 * Merge two lists: local and from server
 */
fun interface ItemsListsMerger {
    fun mergeLists(
        local: List<ru.gribbirg.domain.model.TodoItem>,
        internet: List<ru.gribbirg.domain.model.TodoItem>,
        lastUpdateTime: LocalDateTime
    ): List<ru.gribbirg.domain.model.TodoItem>
}