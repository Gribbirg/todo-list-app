package ru.gribbirg.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.gribbirg.domain.model.user.UserData

/**
 * Repository for handle user data
 */
interface LoginRepository {
    fun getLoginFlow(): Flow<UserData?>

    suspend fun registerUser(key: String)

    suspend fun removeLogin()

    suspend fun isLogin(): Boolean
}