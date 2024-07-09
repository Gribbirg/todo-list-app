package ru.gribbirg.todoapp.data.network.dto

import kotlinx.serialization.Serializable

/**
 * Dto for items list response
 */
@Serializable
data class TodoListResponseDto(
    override val status: String,
    val list: List<TodoItemDto>,
    override val revision: Int,
) : ResponseDto
