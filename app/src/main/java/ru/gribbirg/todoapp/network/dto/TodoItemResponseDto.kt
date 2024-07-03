package ru.gribbirg.todoapp.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class TodoItemResponseDto(
    val status: String,
    val element: TodoItemDto,
    val revision: Int,
)
