package ru.gribbirg.domain.utils

interface SystemDataProvider {
    suspend fun getDeviceId(): String
}