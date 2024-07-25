package ru.gribbirg.network

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.toByteArray
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okio.IOException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import ru.gribbirg.domain.model.todo.TodoItem
import ru.gribbirg.domain.utils.KeyValueDataSaver
import ru.gribbirg.network.dto.TodoItemRequestDto
import ru.gribbirg.network.dto.TodoItemResponseDto
import ru.gribbirg.network.dto.TodoListRequestDto
import ru.gribbirg.network.dto.TodoListResponseDto
import ru.gribbirg.network.dto.toNetworkDto
import ru.gribbirg.utils.extensions.currentLocalDateTimeAtUtc
import ru.gribbirg.utils.extensions.toTimestamp
import java.util.UUID

class ItemsApiClientImplTest {

    private fun mockSaver(revision: String?, lastUpdateTime: String?): KeyValueDataSaver =
        mockk(relaxed = true) {
            coEvery { this@mockk.get(NetworkConstants.LAST_REVISION) } returns revision
            coEvery { this@mockk.get(NetworkConstants.LAST_UPDATE_TIME) } returns lastUpdateTime
            coEvery { this@mockk.get(NetworkConstants.USER_API_KEY) } returns null
        }

    @Test
    fun `get items first time`() = runTest {
        val list = listOf(TodoItem(id = UUID.randomUUID().toString()).toNetworkDto("1"))
        val response = TodoListResponseDto(
            status = "ok",
            revision = 1,
            list = list
        )
        val engine = MockEngine { _ ->
            respond(
                content = Json.encodeToString(
                    response
                ),
                status = HttpStatusCode.OK,
                headers = headers {
                    append(
                        HttpHeaders.ContentType,
                        "application/json; charset=utf-8"
                    )
                }
            )
        }
        val saver = mockSaver(null, null)
        val apiClient = ItemsApiClientImpl(saver, getMainHttpClient(engine))

        val res = apiClient.getAll()

        assertEquals(ApiResponse.Success(response), res)
    }

    @Test
    fun `get items with server error`() = runTest {
        val engine = MockEngine { _ ->
            respond(
                content = "",
                status = HttpStatusCode.InternalServerError,
                headers = headers {
                    append(
                        HttpHeaders.ContentType,
                        "application/json; charset=utf-8"
                    )
                }
            )
        }
        val saver = mockSaver(null, null)
        val apiClient = ItemsApiClientImpl(saver, getMainHttpClient(engine))

        val res = apiClient.getAll()

        assertEquals(ApiResponse.Error.HttpError(500, ""), res)
    }

    @Test
    fun `get items with serialization error`() = runTest {
        val engine = MockEngine { _ ->
            respond(
                content = "not serialized",
                status = HttpStatusCode.OK,
                headers = headers {
                    append(
                        HttpHeaders.ContentType,
                        "application/json; charset=utf-8"
                    )
                }
            )
        }
        val saver = mockSaver(null, null)
        val apiClient = ItemsApiClientImpl(saver, getMainHttpClient(engine))

        val res = apiClient.getAll()

        assertEquals(ApiResponse.Error.SerializationError, res)
    }

    @Test
    fun `get items without network`() = runTest {
        val engine = MockEngine { _ ->
            throw IOException()
        }
        val saver = mockSaver(null, null)
        val apiClient = ItemsApiClientImpl(saver, getMainHttpClient(engine))

        val res = apiClient.getAll()

        assertEquals(ApiResponse.Error.NetworkError, res)
    }

    @Test
    fun `get items and check last update data on error`() = runTest {
        val engine = MockEngine { _ ->
            throw IOException()
        }
        val saver = mockSaver("2", "100")
        val apiClient = ItemsApiClientImpl(saver, getMainHttpClient(engine))

        apiClient.getAll()
        val res = apiClient.lastUpdateTime

        assertTrue(res.toTimestamp().toString() == "100")
    }

    @Test
    fun `get items and check last update data on success`() = runTest {
        val startTime = currentLocalDateTimeAtUtc
        val list = listOf(TodoItem(id = UUID.randomUUID().toString()).toNetworkDto("1"))
        val response = TodoListResponseDto(
            status = "ok",
            revision = 1,
            list = list
        )
        val engine = MockEngine { _ ->
            respond(
                content = Json.encodeToString(
                    response
                ),
                status = HttpStatusCode.OK,
                headers = headers {
                    append(
                        HttpHeaders.ContentType,
                        "application/json; charset=utf-8"
                    )
                }
            )
        }
        val saver = mockSaver(null, null)
        val apiClient = ItemsApiClientImpl(saver, getMainHttpClient(engine))

        apiClient.getAll()
        val res = apiClient.lastUpdateTime

        coVerify { saver.save(NetworkConstants.LAST_REVISION, "1") }
        coVerify { saver.save(NetworkConstants.LAST_UPDATE_TIME, res.toTimestamp().toString()) }
        assertTrue(startTime.toTimestamp() < res.toTimestamp())
    }

    @Test
    fun `add new item`() = runTest {
        val item = TodoItem(id = UUID.randomUUID().toString()).toNetworkDto("1")
        val response = TodoItemResponseDto(
            element = item,
            revision = 2,
            status = "ok",
        )
        val expectedRequest = TodoItemRequestDto(
            element = item,
        )

        val engine = MockEngine { request ->
            respond(
                content = if (request.body.toByteArray()
                        .toString(Charsets.UTF_8) == Json.encodeToString(expectedRequest)
                )
                    Json.encodeToString(response)
                else
                    "",
                status = HttpStatusCode.OK,
                headers = headers {
                    append(
                        HttpHeaders.ContentType,
                        "application/json; charset=utf-8"
                    )
                }
            )
        }
        val saver = mockSaver("1", null)
        val apiClient = ItemsApiClientImpl(saver, getMainHttpClient(engine))

        val res = apiClient.add(item)

        assertEquals(ApiResponse.Success(response), res)
    }

    @Test
    fun `add item with wrong revision`() = runTest {
        val item = TodoItem(id = UUID.randomUUID().toString()).toNetworkDto("1")
        val engine = MockEngine { _ ->
            respond(
                content = NetworkConstants.UNSIGNED_DATA_ERROR,
                status = HttpStatusCode.BadRequest,
                headers = headers {
                    append(
                        HttpHeaders.ContentType,
                        "application/json; charset=utf-8"
                    )
                }
            )
        }
        val saver = mockSaver("1", null)
        val apiClient = ItemsApiClientImpl(saver, getMainHttpClient(engine))

        val res = apiClient.add(item)

        assertEquals(ApiResponse.Error.WrongRevision, res)
    }

    @Test
    fun `update item`() = runTest {
        val item = TodoItem(id = UUID.randomUUID().toString()).toNetworkDto("1")
        val response = TodoItemResponseDto(
            element = item,
            revision = 2,
            status = "ok",
        )
        val expectedRequest = TodoItemRequestDto(
            element = item,
        )

        val engine = MockEngine { request ->
            respond(
                content = if (request.body.toByteArray()
                        .toString(Charsets.UTF_8) == Json.encodeToString(expectedRequest)
                )
                    Json.encodeToString(response)
                else
                    "",
                status = HttpStatusCode.OK,
                headers = headers {
                    append(
                        HttpHeaders.ContentType,
                        "application/json; charset=utf-8"
                    )
                }
            )
        }
        val saver = mockSaver("1", null)
        val apiClient = ItemsApiClientImpl(saver, getMainHttpClient(engine))

        val res = apiClient.update(item)

        assertEquals(ApiResponse.Success(response), res)
    }

    @Test
    fun `update item with not found error`() = runTest {
        val item = TodoItem(id = UUID.randomUUID().toString()).toNetworkDto("1")

        val engine = MockEngine { _ ->
            respond(
                content = "",
                status = HttpStatusCode.NotFound,
                headers = headers {
                    append(
                        HttpHeaders.ContentType,
                        "application/json; charset=utf-8"
                    )
                }
            )
        }
        val saver = mockSaver("1", null)
        val apiClient = ItemsApiClientImpl(saver, getMainHttpClient(engine))

        val res = apiClient.update(item)

        assertEquals(ApiResponse.Error.NotFound, res)
    }

    @Test
    fun `delete item`() = runTest {
        val item = TodoItem(id = UUID.randomUUID().toString()).toNetworkDto("1")
        val response = TodoItemResponseDto(
            element = item,
            revision = 2,
            status = "ok",
        )

        val engine = MockEngine { request ->
            respond(
                content = if (
                    request.url.encodedPath.split("/").last() == item.id
                )
                    Json.encodeToString(response)
                else
                    "",
                status = HttpStatusCode.OK,
                headers = headers {
                    append(
                        HttpHeaders.ContentType,
                        "application/json; charset=utf-8"
                    )
                }
            )
        }
        val saver = mockSaver("1", null)
        val apiClient = ItemsApiClientImpl(saver, getMainHttpClient(engine))

        val res = apiClient.delete(item.id)

        assertEquals(ApiResponse.Success(response), res)
    }

    @Test
    fun `refresh all items`() = runTest {
        val list = listOf(TodoItem(id = UUID.randomUUID().toString()).toNetworkDto("1"))
        val expectedRequest = TodoListRequestDto(
            list = list
        )
        val response = TodoListResponseDto(
            status = "ok",
            revision = 1,
            list = list
        )
        val engine = MockEngine { request ->
            println(
                request.body.toByteArray().toString(Charsets.UTF_8)
            )
            println(
                Json.encodeToString(expectedRequest)
            )
            respond(
                content = if (
                    Json.decodeFromString<TodoListRequestDto>(request.body.toByteArray().toString(Charsets.UTF_8)) ==
                    expectedRequest
                )
                    Json.encodeToString(response)
                else
                    "",
                status = HttpStatusCode.OK,
                headers = headers {
                    append(
                        HttpHeaders.ContentType,
                        "application/json; charset=utf-8"
                    )
                }
            )
        }
        val saver = mockSaver(null, null)
        val apiClient = ItemsApiClientImpl(saver, getMainHttpClient(engine))

        val res = apiClient.refreshAll(list)

        assertEquals(ApiResponse.Success(response), res)
    }
}