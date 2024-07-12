package ru.gribbirg.network

import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.headers
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.gribbirg.domain.utils.KeyValueDataSaver
import ru.gribbirg.network.di.modules.ApiClientImplQualifier
import ru.gribbirg.network.di.modules.ApiClientScope
import ru.gribbirg.network.dto.ResponseDto
import ru.gribbirg.network.dto.TodoItemDto
import ru.gribbirg.network.dto.TodoItemRequestDto
import ru.gribbirg.network.dto.TodoItemResponseDto
import ru.gribbirg.network.dto.TodoListRequestDto
import ru.gribbirg.network.dto.TodoListResponseDto
import ru.gribbirg.utils.extensions.toLocalDateTime
import ru.gribbirg.utils.extensions.toTimestamp
import java.io.IOException
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

/**
 * Implementation with ktor
 */
@ApiClientScope
internal class ItemsApiClientImpl @Inject constructor(
    private val dataStore: KeyValueDataSaver,
    @ApiClientImplQualifier private val coroutineDispatcher: CoroutineDispatcher,
    @ApiClientImplQualifier private val client: HttpClient = mainHttpClient,
) : ItemsApiClient {

    private var lastRevision = -1
        private set(value) {
            CoroutineScope(coroutineDispatcher).launch {
                dataStore.save(NetworkConstants.LAST_REVISION, value.toString())
            }
            field = value
        }
    override var lastUpdateTime: LocalDateTime = (0L).toLocalDateTime()!!
        private set(value) {
            CoroutineScope(coroutineDispatcher).launch {
                dataStore.save(NetworkConstants.LAST_UPDATE_TIME, value.toTimestamp().toString())
            }
            field = value
        }

    init {
        runBlocking {
            lastRevision = dataStore.get(NetworkConstants.LAST_REVISION)?.toInt() ?: lastRevision
            lastUpdateTime =
                dataStore.get(NetworkConstants.LAST_UPDATE_TIME)?.toLong()?.toLocalDateTime()
                    ?: lastUpdateTime
        }
    }

    override suspend fun getAll(): ApiResponse<TodoListResponseDto> {
        return client.safeRequest<TodoListResponseDto> {
            url(NetworkConstants.LIST_URL)
            method = HttpMethod.Get
        }
    }

    override suspend fun add(item: TodoItemDto): ApiResponse<TodoItemResponseDto> {
        return client.safeRequest<TodoItemResponseDto> {
            url(NetworkConstants.LIST_URL)
            method = HttpMethod.Post
            headers {
                append(NetworkConstants.REVISION_HEADER, lastRevision.toString())
            }
            setBody(Json.encodeToString(TodoItemRequestDto(element = item)))
        }
    }

    override suspend fun update(item: TodoItemDto): ApiResponse<TodoItemResponseDto> {
        return client.safeRequest<TodoItemResponseDto> {
            url(NetworkConstants.getItemUrl(item.id))
            method = HttpMethod.Put
            headers {
                append(NetworkConstants.REVISION_HEADER, lastRevision.toString())
            }
            setBody(Json.encodeToString(TodoItemRequestDto(element = item)))
        }
    }

    override suspend fun delete(itemId: String): ApiResponse<TodoItemResponseDto> {
        return client.safeRequest<TodoItemResponseDto> {
            url(NetworkConstants.getItemUrl(itemId))
            method = HttpMethod.Delete
            headers {
                append(NetworkConstants.REVISION_HEADER, lastRevision.toString())
            }
        }
    }

    override suspend fun refreshAll(items: List<TodoItemDto>): ApiResponse<TodoListResponseDto> {
        return client.safeRequest<TodoListResponseDto> {
            url(NetworkConstants.LIST_URL)
            method = HttpMethod.Patch
            headers {
                append(NetworkConstants.REVISION_HEADER, lastRevision.toString())
            }
            setBody(TodoListRequestDto(list = items))
        }
    }

    private suspend inline fun <reified T : ResponseDto> HttpClient.safeRequest(
        crossinline block: HttpRequestBuilder.() -> Unit,
    ): ApiResponse<T> = withContext(coroutineContext) {
        runBlocking {
            val key = dataStore.get(NetworkConstants.USER_API_KEY) ?: ""

            return@runBlocking try {
                val response = request {
                    block()
                    headers {
                        append(
                            HttpHeaders.Authorization,
                            NetworkConstants.getAuthHeader(key)
                        )
                    }
                }
                ApiResponse.Success(response.body<T>())
            } catch (clientRequestException: ClientRequestException) {
                handleClientRequestError(clientRequestException)
            } catch (serverResponseException: ServerResponseException) {
                ApiResponse.Error.HttpError(
                    serverResponseException.response.status.value,
                    serverResponseException.response.body()
                )
            } catch (ioException: IOException) {
                ApiResponse.Error.NetworkError
            } catch (serializationException: SerializationException) {
                ApiResponse.Error.SerializationError
            } catch (noTransformationFoundException: NoTransformationFoundException) {
                ApiResponse.Error.SerializationError
            } catch (e: Exception) {
                ApiResponse.Error.UnknownError
            }.updateLastRequestData()
        }
    }

    private suspend fun handleClientRequestError(exception: ClientRequestException) =
        when {
            exception.response.status.value == 400 && exception.response.body<String>() == NetworkConstants.UNSIGNED_DATA_ERROR ->
                ApiResponse.Error.WrongRevision

            exception.response.status.value == 401 -> ApiResponse.Error.Unauthorized
            exception.response.status.value == 404 -> ApiResponse.Error.NotFound

            else -> ApiResponse.Error.HttpError(
                exception.response.status.value,
                exception.response.body()
            )
        }


    private inline fun <reified T : ResponseDto> ApiResponse<T>.updateLastRequestData(): ApiResponse<T> =
        also {
            if (it is ApiResponse.Success) {
                lastRevision = it.body.revision
                lastUpdateTime = LocalDateTime.now(ZoneId.of("UTC"))
            }
        }
}