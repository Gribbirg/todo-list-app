package ru.gribbirg.todoapp.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class TodoListResponseDto(
    val status: String,
    val list: List<TodoItemDto>,
    val revision: Int,
)
