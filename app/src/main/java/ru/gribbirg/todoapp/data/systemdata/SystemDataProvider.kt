package ru.gribbirg.todoapp.data.systemdata

interface SystemDataProvider {
    suspend fun getDeviceId(): String
}