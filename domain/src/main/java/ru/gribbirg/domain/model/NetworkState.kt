package ru.gribbirg.domain.model

import androidx.annotation.StringRes
import ru.gribbirg.todoapp.domain.R

/**
 * Network state from repo to view model
 *
 * @see TodoItemRepositoryImpl
 */
sealed class NetworkState {
    data object Updating : NetworkState()
    data object Success : NetworkState()
    sealed class Error(@StringRes val messageId: Int) : NetworkState() {
        data object NetworkError : Error(R.string.network_error)
        data object UnknownError : Error(R.string.network_unknown_error)
    }
}