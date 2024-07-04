package ru.gribbirg.todoapp.network

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.headers
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.gribbirg.todoapp.BuildConfig
import ru.gribbirg.todoapp.data.datestore.DataStoreUtil
import ru.gribbirg.todoapp.network.dto.ResponseDto
import ru.gribbirg.todoapp.network.dto.TodoItemDto
import ru.gribbirg.todoapp.network.dto.TodoItemRequestDto
import ru.gribbirg.todoapp.network.dto.TodoItemResponseDto
import ru.gribbirg.todoapp.network.dto.TodoListRequestDto
import ru.gribbirg.todoapp.network.dto.TodoListResponseDto
import ru.gribbirg.todoapp.utils.toLocalDateTime
import ru.gribbirg.todoapp.utils.toTimestamp
import java.io.IOException
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

class ApiClient(
    private val dataStore: DataStoreUtil,
    private val coroutineContext: CoroutineContext =
        Executors.newSingleThreadExecutor().asCoroutineDispatcher(),
) {
    private val client = HttpClient(Android) {
        expectSuccess = true

        install(ContentNegotiation) {
            json(json = Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
                useAlternativeNames = true
            })
        }

        install(HttpRequestRetry) {
            exponentialDelay(
                base = 1.1,
                maxDelayMs = 1000,
            )
            retryIf(3) { _, response ->
                response.status.let { !it.isSuccess() && it.value !in 400..499 }
            }
        }

        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    Log.v("internet", "Ktor: $message")
                }
            }
        }

        install(ResponseObserver) {
            onResponse { response ->
                Log.d("internet", "HTTP status: ${response.status.value}")
            }
        }

        install(DefaultRequest) {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "OAuth ${BuildConfig.API_KEY}")
            }
        }
    }

    private var lastRevision = -1
        private set(value) {
            CoroutineScope(coroutineContext).launch {
                dataStore.saveItem("lastRevision", value.toString())
            }
            field = value
        }
    var lastUpdateTime: LocalDateTime = (0L).toLocalDateTime()!!
        private set(value) {
            CoroutineScope(coroutineContext).launch {
                dataStore.saveItem("lastUpdateTime", value.toTimestamp().toString())
            }
            field = value
        }

    init {
        runBlocking {
            lastRevision = dataStore.getItem("lastRevision")?.toInt() ?: lastRevision
            lastUpdateTime = dataStore.getItem("lastUpdateTime")?.toLong()?.toLocalDateTime() ?: lastUpdateTime
        }
    }

    suspend fun getAll(): ApiResponse<TodoListResponseDto> {
        return client.safeRequest<TodoListResponseDto> {
            url(LIST_URL)
            method = HttpMethod.Get
        }.updateLastRequestData()
    }

    suspend fun add(item: TodoItemDto): ApiResponse<TodoItemResponseDto> {
        return client.safeRequest<TodoItemResponseDto> {
            url(LIST_URL)
            method = HttpMethod.Put
            headers {
                append(REVISION_HEADER, lastRevision.toString())
            }
            setBody(Json.encodeToString(TodoItemRequestDto(element = item)))
        }
    }

    suspend fun update(item: TodoItemDto): ApiResponse<TodoItemResponseDto> {
        return client.safeRequest<TodoItemResponseDto> {
            url(getItemUrl(item.id))
            method = HttpMethod.Put
            headers {
                append(REVISION_HEADER, lastRevision.toString())
            }
            setBody(Json.encodeToString(TodoItemRequestDto(element = item)))
        }.updateLastRequestData()
    }

    suspend fun delete(itemId: String): ApiResponse<TodoItemResponseDto> {
        return client.safeRequest<TodoItemResponseDto> {
            url(getItemUrl(itemId))
            method = HttpMethod.Delete
            headers {
                append(REVISION_HEADER, lastRevision.toString())
            }
        }.updateLastRequestData()
    }

    suspend fun refreshAll(items: List<TodoItemDto>): ApiResponse<TodoListResponseDto> {
        return client.safeRequest<TodoListResponseDto> {
            url(LIST_URL)
            method = HttpMethod.Patch
            headers {
                append(REVISION_HEADER, lastRevision.toString())
            }
            setBody(TodoListRequestDto(list = items))
        }.updateLastRequestData()
    }

    private suspend inline fun <reified T> HttpClient.safeRequest(
        crossinline block: HttpRequestBuilder.() -> Unit,
    ): ApiResponse<T> = withContext(coroutineContext) {
        return@withContext try {
            val response = request { block() }
            ApiResponse.Success(response.body())
        } catch (e: ClientRequestException) {
            if (e.response.status.value == 400 && e.response.body<String>() == "unsynchronized data")
                ApiResponse.Error.WrongRevisionError
            else
                ApiResponse.Error.HttpError(e.response.status.value, e.response.body())
        } catch (e: ServerResponseException) {
            ApiResponse.Error.HttpError(e.response.status.value, e.response.body())
        } catch (e: IOException) {
            ApiResponse.Error.NetworkError
        } catch (e: SerializationException) {
            ApiResponse.Error.SerializationError
        } catch (e: NoTransformationFoundException) {
            ApiResponse.Error.SerializationError
        }
    }


    private inline fun <reified T : ResponseDto> ApiResponse<T>.updateLastRequestData() =
        also {
            if (it is ApiResponse.Success) {
                lastRevision = it.body.revision
                if (it.body is TodoListResponseDto)
                    lastUpdateTime = LocalDateTime.now(ZoneId.of("UTC"))
            }
        }
}