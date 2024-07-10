package ru.gribbirg.todoapp.data.repositories.login

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import ru.gribbirg.todoapp.data.data.UserData
import ru.gribbirg.todoapp.data.keyvaluesaver.KeyValueDataSaver
import ru.gribbirg.todoapp.data.network.NetworkConstants
import ru.gribbirg.todoapp.di.ApiClientKeyValueSaverQualifier
import ru.gribbirg.todoapp.di.BackgroundDispatcher
import javax.inject.Inject

/**
 * Implementation with saving value in key value store
 *
 * @see LoginRepository
 */
class LoginRepositoryImpl @Inject constructor(
    @ApiClientKeyValueSaverQualifier private val internetDataStore: KeyValueDataSaver,
    @BackgroundDispatcher private val coroutineDispatcher: CoroutineDispatcher,
) : LoginRepository {

    private val _loginFlow: MutableStateFlow<UserData?> = MutableStateFlow(null)

    override fun getLoginFlow(): Flow<UserData?> = _loginFlow.asStateFlow()

    override suspend fun registerUser(key: String) = withContext(coroutineDispatcher) {
        internetDataStore.save(NetworkConstants.USER_API_KEY, key)
        _loginFlow.update { UserData() }
    }

    override suspend fun removeLogin() = withContext(coroutineDispatcher) {
        internetDataStore.remove(NetworkConstants.USER_API_KEY)
        internetDataStore.remove(NetworkConstants.LAST_REVISION)
        internetDataStore.remove(NetworkConstants.LAST_UPDATE_TIME)
        _loginFlow.update { null }
    }


    override suspend fun isLogin(): Boolean = withContext(coroutineDispatcher) {
        return@withContext internetDataStore.get(NetworkConstants.USER_API_KEY) != null
    }
}