package ru.gribbirg.network

import ru.gribbirg.network.dto.TodoItemDto
import ru.gribbirg.network.dto.TodoItemResponseDto
import ru.gribbirg.network.dto.TodoListResponseDto
import java.time.LocalDateTime

/**
 * Api client for cloud storage of items
 */
interface ItemsApiClient {
    val lastUpdateTime: LocalDateTime

    suspend fun getAll(): ApiResponse<TodoListResponseDto>

    suspend fun add(item: TodoItemDto): ApiResponse<TodoItemResponseDto>

    suspend fun update(item: TodoItemDto): ApiResponse<TodoItemResponseDto>

    suspend fun delete(itemId: String): ApiResponse<TodoItemResponseDto>

    suspend fun refreshAll(items: List<TodoItemDto>): ApiResponse<TodoListResponseDto>
}