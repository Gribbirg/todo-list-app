package ru.gribbirg.todoapp.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class TodoItemResponseDto(
    override val status: String,
    val element: TodoItemDto,
    override val revision: Int,
) : ResponseDto()
