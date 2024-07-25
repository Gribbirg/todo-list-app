package ru.gribbirg.data

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import ru.gribbirg.domain.model.user.UserData
import ru.gribbirg.domain.utils.KeyValueDataSaver
import ru.gribbirg.network.NetworkConstants

class LoginRepositoryImplTest {

    private val dispatcher = StandardTestDispatcher()

    private fun mockDataSaver(apiKey: String? = null): KeyValueDataSaver =
        mockk(relaxed = true) {
            coEvery { this@mockk.get(NetworkConstants.USER_API_KEY) } returns apiKey
        }

    @Test
    fun `register user and check from flow`() = runTest(dispatcher) {
        val key = "1111"
        val saver = mockDataSaver(null)
        val repo = LoginRepositoryImpl(saver, dispatcher)

        repo.registerUser(key)

        coVerify { saver.save(NetworkConstants.USER_API_KEY, key) }
        assertEquals(UserData(), repo.getLoginFlow().first())
    }

    @Test
    fun `remove login and check from flow`() = runTest(dispatcher) {
        val key = "1111"
        val saver = mockDataSaver(key)
        val repo = LoginRepositoryImpl(saver, dispatcher)

        repo.removeLogin()

        coVerify { saver.remove(NetworkConstants.USER_API_KEY) }
        coVerify { saver.remove(NetworkConstants.LAST_REVISION) }
        coVerify { saver.remove(NetworkConstants.LAST_UPDATE_TIME) }
        assertEquals(null, repo.getLoginFlow().first())
    }

    @Test
    fun `is user login`() = runTest(dispatcher) {
        val key = "1111"
        val saver = mockDataSaver(key)

        val repo = LoginRepositoryImpl(saver, dispatcher)

        assertTrue(repo.isLogin())
    }
}