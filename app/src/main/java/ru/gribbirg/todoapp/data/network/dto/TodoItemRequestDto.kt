package ru.gribbirg.todoapp.data.network.dto

import kotlinx.serialization.Serializable

/**
 * Dto for request with single item
 */
@Serializable
data class TodoItemRequestDto(
    val element: TodoItemDto,
)
