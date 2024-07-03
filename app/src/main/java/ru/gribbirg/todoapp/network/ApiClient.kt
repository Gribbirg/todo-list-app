package ru.gribbirg.todoapp.network

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.gribbirg.todoapp.BuildConfig
import ru.gribbirg.todoapp.network.dto.TodoItemDto
import ru.gribbirg.todoapp.network.dto.TodoItemRequestDto
import ru.gribbirg.todoapp.network.dto.TodoListResponseDto

class ApiClient {
    private val client = HttpClient(Android) {
        HttpClient {
            install(ContentNegotiation) {
                json(json = Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }

            install(HttpRequestRetry) {
                retryOnServerErrors(5)
                exponentialDelay()
            }

            install(Logging) {
                level = LogLevel.ALL
            }

            install(DefaultRequest) {
                contentType(ContentType.Application.Json)
            }
        }
    }

    suspend fun getAll(): TodoListResponseDto {
        val res: TodoListResponseDto = client.get(LIST_URL) {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "OAuth ${BuildConfig.API_KEY}")
                append(HttpHeaders.ContentType, "application/json")
            }
        }
            .body<String>()
            .let { Json.decodeFromString(it) }
        return res
    }

    suspend fun add(item: TodoItemDto, revision: Int) {
        client.post(LIST_URL) {
            headers {
                append(HttpHeaders.Authorization, "OAuth ${BuildConfig.API_KEY}")
                append("X-Last-Known-Revision", revision.toString())
            }
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(TodoItemRequestDto(element = item)))
        }
    }

    suspend fun update(item: TodoItemDto, revision: Int) {
        client.put(getItemUrl(item.id)) {
            headers {
                append(HttpHeaders.Authorization, "OAuth ${BuildConfig.API_KEY}")
                append("X-Last-Known-Revision", revision.toString())
            }
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(TodoItemRequestDto(element = item)))
        }
    }

    suspend fun delete(itemId: String, revision: Int) {
        client.delete(getItemUrl(itemId)) {
            headers {
                append(HttpHeaders.Authorization, "OAuth ${BuildConfig.API_KEY}")
                append("X-Last-Known-Revision", revision.toString())
            }
            contentType(ContentType.Application.Json)
        }.also { Log.i("test", "delete: $it") }
    }
}