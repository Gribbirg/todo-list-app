package ru.gribbirg.todoapp.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class TodoItemRequestDto(
    val element: TodoItemDto,
)
