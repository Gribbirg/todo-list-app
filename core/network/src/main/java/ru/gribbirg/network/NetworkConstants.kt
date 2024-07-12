package ru.gribbirg.network

/**
 * Constants for internet connection
 */
object NetworkConstants {
    internal const val BASE_URL = "https://hive.mrdekk.ru/todo"
    internal const val LIST_URL = "$BASE_URL/list"
    internal fun getItemUrl(id: String) = "$LIST_URL/$id"

    internal const val REVISION_HEADER = "X-Last-Known-Revision"

    const val LAST_UPDATE_TIME = "last_update_time"
    const val LAST_REVISION = "last_revision"

    const val USER_API_KEY = "user_api_key"
    internal fun getAuthHeader(key: String) = "OAuth $key"

    internal const val UNSIGNED_DATA_ERROR = "unsynchronized data"

    const val KEY_VALUE_SAVER_NAME = "internet_data"
}