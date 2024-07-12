package ru.gribbirg.network

/**
 * Api response from client to repo
 */
sealed class ApiResponse<out T> {
    data class Success<T>(val body: T) : ApiResponse<T>()
    sealed class Error : ApiResponse<Nothing>() {
        data object WrongRevision : Error()
        data class HttpError(val code: Int, val errorBody: String?) : Error()
        data object NetworkError : Error()
        data object SerializationError : Error()
        data object Unauthorized : Error()
        data object NotFound : Error()
        data object UnknownError : Error()
    }
}