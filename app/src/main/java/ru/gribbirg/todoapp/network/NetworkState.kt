package ru.gribbirg.todoapp.network

sealed class NetworkState {
    data object Loading: NetworkState()
    data object Updating: NetworkState()
    data object Success: NetworkState()
    data class Error(val exception: Throwable): NetworkState()
}
