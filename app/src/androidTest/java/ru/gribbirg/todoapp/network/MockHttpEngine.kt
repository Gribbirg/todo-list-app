package ru.gribbirg.todoapp.network

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.gribbirg.domain.model.todo.TodoImportance
import ru.gribbirg.domain.model.todo.TodoItem
import ru.gribbirg.network.dto.TodoItemResponseDto
import ru.gribbirg.network.dto.TodoListResponseDto
import ru.gribbirg.network.dto.toNetworkDto
import java.time.LocalDate
import java.time.LocalDateTime

internal val mockEngine =
    MockEngine { request ->
        when (request.url.pathSegments.last()) {
            "list" -> when (request.method) {
                HttpMethod.Get, HttpMethod.Patch -> respond(
                    content = Json.encodeToString(
                        TodoListResponseDto(
                            status = "ok",
                            revision = 1,
                            list = items
                        )
                    ),
                    status = HttpStatusCode.OK,
                    headers = headers,
                )

                HttpMethod.Post -> respond(
                    content = Json.encodeToString(
                        TodoItemResponseDto(
                            status = "ok",
                            revision = 2,
                            element = extraItem,
                        )
                    ),
                    status = HttpStatusCode.OK,
                    headers = headers
                )

                HttpMethod.Put -> respond(
                    content = Json.encodeToString(
                        TodoItemResponseDto(
                            status = "ok",
                            revision = 2,
                            element = firstEdited,
                        )
                    ),
                    status = HttpStatusCode.OK,
                    headers = headers
                )

                else -> respond(
                    content = "",
                    status = HttpStatusCode.NotFound,
                    headers = headers
                )
            }

            else -> respond(
                content = "",
                status = HttpStatusCode.NotFound,
                headers = headers
            )
        }
    }


private val headers = headers {
    append(
        HttpHeaders.ContentType,
        "application/json; charset=utf-8"
    )
}

val items = List(5) {
    TodoItem(
        id = it.toString(),
        text = "Дело $it ".repeat(it * 5 + 1),
        importance = TodoImportance.entries[it % 3],
        deadline = if (it % 3 == 0)
            null
        else LocalDate.now().plusDays((-100L..100L).random()),
        completed = it % 4 == 0,
        creationDate = LocalDateTime.now().minusDays((-0L..100L).random()),
        editDate = LocalDateTime.now().minusDays((-0L..100L).random()),
    ).toNetworkDto("1")
}

val extraItem = TodoItem(
    id = "-1",
).toNetworkDto("1")

val firstEdited = items.first().copy(text = "New text")