package ru.gribbirg.todoapp.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class TodoListResponseDto(
    override val status: String,
    val list: List<TodoItemDto>,
    override val revision: Int,
) : ResponseDto()
