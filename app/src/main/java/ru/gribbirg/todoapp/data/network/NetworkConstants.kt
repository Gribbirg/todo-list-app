package ru.gribbirg.todoapp.data.network

/**
 * Constants for internet connection
 */
object NetworkConstants {
    const val BASE_URL = "https://hive.mrdekk.ru/todo"
    const val LIST_URL = "$BASE_URL/list"
    fun getItemUrl(id: String) = "$LIST_URL/$id"

    const val REVISION_HEADER = "X-Last-Known-Revision"

    const val LAST_UPDATE_TIME = "last_update_time"
    const val LAST_REVISION = "last_revision"

    const val USER_API = "user_api_key"
    fun getAuthHeader(key: String) = "OAuth $key"

    const val UNSIGNED_DATA_ERROR = "unsynchronized data"
}