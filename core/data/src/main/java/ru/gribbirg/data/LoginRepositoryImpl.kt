package ru.gribbirg.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.gribbirg.data.di.DataScope
import ru.gribbirg.data.di.modules.BackgroundDispatcher
import ru.gribbirg.domain.model.user.UserData
import ru.gribbirg.domain.repositories.LoginRepository
import ru.gribbirg.domain.utils.KeyValueDataSaver
import ru.gribbirg.network.NetworkConstants
import javax.inject.Inject

/**
 * Implementation with saving value in key value store
 *
 * @see LoginRepository
 */
@DataScope
class LoginRepositoryImpl @Inject constructor(
    private val internetDataStore: KeyValueDataSaver,
    @BackgroundDispatcher private val coroutineDispatcher: CoroutineDispatcher,
) : LoginRepository {

    private val _loginFlow: MutableStateFlow<UserData?> =
        MutableStateFlow(null)

    init {
        CoroutineScope(coroutineDispatcher).launch {
            _loginFlow.update {
                internetDataStore
                    .get(NetworkConstants.USER_API_KEY)
                    ?.let {
                        UserData()
                    }
            }
        }
    }

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