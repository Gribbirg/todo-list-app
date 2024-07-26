package ru.gribbirg.settings

import com.yandex.authsdk.YandexAuthException
import com.yandex.authsdk.YandexAuthResult
import com.yandex.authsdk.YandexAuthToken
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.yield
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.gribbirg.domain.model.user.ThemeSettings
import ru.gribbirg.domain.model.user.UserData
import ru.gribbirg.domain.model.user.UserSettings
import ru.gribbirg.domain.repositories.LoginRepository
import ru.gribbirg.domain.utils.SettingsHandler

class SettingsViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private val testToken = "1"

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setDispatchers() {
        Dispatchers.setMain(dispatcher)
    }

    @Test
    fun `collect login`() = runTest(dispatcher) {
        val user = UserData()
        val login: LoginRepository = mockk(relaxed = true) {
            every { getLoginFlow() } returns MutableStateFlow(user).asStateFlow()
        }
        val viewModel = SettingsViewModel(
            loginRepository = login,
            settingsHandler = mockk(relaxed = true),
            dispatcher
        )

        repeat(5) { yield() }
        val res = viewModel.uiState.value.loginState

        assertEquals(SettingsUiState.LoginState.Auth(user), res)
    }

    @Test
    fun `collect login with error`() = runTest(dispatcher) {
        val login: LoginRepository = mockk(relaxed = true) {
            every { getLoginFlow() } returns flow { throw Exception() }
        }
        val viewModel = SettingsViewModel(
            loginRepository = login,
            settingsHandler = mockk(relaxed = true),
            dispatcher
        )

        repeat(5) { yield() }
        val res = viewModel.uiState.value.loginState

        assertTrue(res is SettingsUiState.LoginState.Error)
    }

    @Test
    fun `collect settings`() = runTest(dispatcher) {
        val settings = UserSettings()
        val settingsHandler: SettingsHandler = mockk(relaxed = true) {
            every { getSettings() } returns MutableStateFlow(settings).asStateFlow()
        }
        val viewModel = SettingsViewModel(
            loginRepository = mockk(relaxed = true),
            settingsHandler = settingsHandler,
            dispatcher
        )

        repeat(5) { yield() }
        val res = viewModel.uiState.value.appSettingsState

        assertEquals(SettingsUiState.AppSettingsState.Loaded(settings), res)
    }

    @Test
    fun `successful login`() = runTest(dispatcher) {
        val login: LoginRepository = mockk(relaxed = true)
        val viewModel = SettingsViewModel(
            loginRepository = login,
            settingsHandler = mockk(relaxed = true),
            dispatcher
        )
        val loginRes = YandexAuthResult.Success(YandexAuthToken(testToken, 1L))

        viewModel.onLogin(loginRes)
        yield()

        coVerify { login.registerUser(testToken) }
    }

    @Test
    fun `failure login`() = runTest(dispatcher) {
        val login: LoginRepository = mockk(relaxed = true)
        val viewModel = SettingsViewModel(
            loginRepository = login,
            settingsHandler = mockk(relaxed = true),
            dispatcher
        )
        val loginRes = YandexAuthResult.Failure(YandexAuthException(""))

        viewModel.onLogin(loginRes)
        yield()

        assertTrue(viewModel.uiState.value.loginState is SettingsUiState.LoginState.Error)
    }

    @Test
    fun logout() = runTest(dispatcher) {
        val login: LoginRepository = mockk(relaxed = true)
        val viewModel = SettingsViewModel(
            loginRepository = login,
            settingsHandler = mockk(relaxed = true),
            dispatcher
        )

        viewModel.onLogout()
        yield()

        coVerify { login.removeLogin() }
    }

    @Test
    fun `app settings change`() = runTest(dispatcher) {
        val settingsHandler: SettingsHandler = mockk(relaxed = true)
        val viewModel = SettingsViewModel(
            loginRepository = mockk(relaxed = true),
            settingsHandler = settingsHandler,
            dispatcher
        )
        val settings = UserSettings(theme = ThemeSettings.Light)

        viewModel.onAppSettingsChange(settings)
        yield()

        coVerify { settingsHandler.saveSettings(settings) }
    }
}