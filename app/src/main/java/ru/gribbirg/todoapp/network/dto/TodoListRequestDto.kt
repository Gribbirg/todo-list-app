package ru.gribbirg.todoapp.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class TodoListRequestDto(
    val list: List<TodoItemDto>,
)
