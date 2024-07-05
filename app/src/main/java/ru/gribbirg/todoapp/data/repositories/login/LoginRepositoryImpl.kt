package ru.gribbirg.todoapp.data.repositories.login

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import ru.gribbirg.todoapp.data.data.UserData
import ru.gribbirg.todoapp.data.keyvaluesaver.KeyValueDataSaver
import ru.gribbirg.todoapp.data.network.NetworkConstants
import kotlin.coroutines.CoroutineContext

/**
 * Implementation with saving value in key value store
 *
 * @see LoginRepository
 */
class LoginRepositoryImpl(
    private val internetDataStore: KeyValueDataSaver,
    private val coroutineContext: CoroutineContext = Dispatchers.IO,
) : LoginRepository {

    private val _loginFlow: MutableStateFlow<UserData?> = MutableStateFlow(null)

    override fun getLoginFlow(): Flow<UserData?> = _loginFlow.asStateFlow()

    override suspend fun registerUser(key: String) = withContext(coroutineContext) {
        internetDataStore.save(USER_API_KEY, key)
        _loginFlow.update { UserData() }
    }

    override suspend fun removeLogin() = withContext(coroutineContext) {
        internetDataStore.remove(USER_API_KEY)
        internetDataStore.remove(NetworkConstants.LAST_REVISION)
        internetDataStore.remove(NetworkConstants.LAST_UPDATE_TIME)
        _loginFlow.update { null }
    }


    override suspend fun isLogin(): Boolean = withContext(coroutineContext) {
        return@withContext internetDataStore.get(USER_API_KEY) != null
    }

    companion object {
        const val USER_API_KEY = "user_api_key"
    }
}