package ru.gribbirg.todoapp.data.network.dto

import kotlinx.serialization.Serializable

/**
 * Dto for single item response
 */
@Serializable
data class TodoItemResponseDto(
    override val status: String,
    val element: TodoItemDto,
    override val revision: Int,
) : ResponseDto
