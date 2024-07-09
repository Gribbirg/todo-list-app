package ru.gribbirg.todoapp.data.network.dto

import kotlinx.serialization.Serializable

/**
 * Dto for items list request
 */
@Serializable
data class TodoListRequestDto(
    val list: List<TodoItemDto>,
)
