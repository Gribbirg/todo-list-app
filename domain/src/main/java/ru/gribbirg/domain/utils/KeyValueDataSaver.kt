package ru.gribbirg.domain.utils

/**
 * Interface for saving values by keys
 */
interface KeyValueDataSaver {
    suspend fun save(key: String, value: String)

    suspend fun get(key: String): String?

    suspend fun remove(key: String)
}