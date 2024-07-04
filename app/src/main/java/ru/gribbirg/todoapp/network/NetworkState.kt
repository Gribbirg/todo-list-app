package ru.gribbirg.todoapp.network

import androidx.annotation.StringRes
import ru.gribbirg.todoapp.R

sealed class NetworkState {
    data object Loading : NetworkState()
    data object Updating : NetworkState()
    data object Success : NetworkState()
    sealed class Error(@StringRes val messageId: Int) : NetworkState() {
        data object NetworkError : Error(R.string.network_error)
        data object UnknownError : Error(R.string.network_unknown_error)
    }
}

sealed class ApiResponse<out T> {
    data class Success<T>(val body: T) : ApiResponse<T>()
    sealed class Error : ApiResponse<Nothing>() {
        data object WrongRevisionError : Error()
        data class HttpError(val code: Int, val errorBody: String?) : Error()
        data object NetworkError : Error()
        data object SerializationError : Error()
    }
}