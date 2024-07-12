package ru.gribbirg.domain.repositories

import kotlinx.coroutines.flow.Flow

/**
 * Repository for handle user data
 */
interface LoginRepository {
    fun getLoginFlow(): Flow<ru.gribbirg.domain.model.UserData?>

    suspend fun registerUser(key: String)

    suspend fun removeLogin()

    suspend fun isLogin(): Boolean
}