package ru.gribbirg.todoapp.data.repositories

import kotlinx.coroutines.flow.Flow
import ru.gribbirg.todoapp.data.data.UserData

interface LoginRepository {
    fun getLoginFlow(): Flow<UserData?>

    suspend fun registerUser(key: String)

    suspend fun removeLogin()

    suspend fun isLogin(): Boolean
}