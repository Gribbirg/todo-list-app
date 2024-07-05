package ru.gribbirg.todoapp.data.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import ru.gribbirg.todoapp.data.data.UserData
import ru.gribbirg.todoapp.data.datestore.DataStoreUtil
import kotlin.coroutines.CoroutineContext

class LoginRepositoryImpl(
    private val internetDataStore: DataStoreUtil,
    private val coroutineContext: CoroutineContext = Dispatchers.IO,
) : LoginRepository {

    private val _loginFlow: MutableStateFlow<UserData?> = MutableStateFlow(null)

    override fun getLoginFlow(): Flow<UserData?> = _loginFlow.asStateFlow()

    override suspend fun registerUser(key: String) = withContext(coroutineContext) {
        internetDataStore.saveItem("user_api_key", key)
        _loginFlow.update { UserData() }
    }

    override suspend fun removeLogin() = withContext(coroutineContext) {
        internetDataStore.removeItem("user_api_key")
        _loginFlow.update { null }
    }


    override suspend fun isLogin(): Boolean = withContext(coroutineContext) {
        return@withContext internetDataStore.getItem("user_api_key") != null
    }
}