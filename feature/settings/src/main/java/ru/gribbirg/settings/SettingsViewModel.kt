package ru.gribbirg.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yandex.authsdk.YandexAuthResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.gribbirg.domain.model.user.UserSettings
import ru.gribbirg.domain.repositories.LoginRepository
import ru.gribbirg.domain.utils.SettingsHandler
import ru.gribbirg.settings.di.modules.BackgroundDispatcher
import javax.inject.Inject
import javax.security.auth.login.LoginException

class SettingsViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val settingsHandler: SettingsHandler,
    @BackgroundDispatcher private val backgroundDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        SettingsUiState(
            SettingsUiState.AppSettingsState.Loading,
            SettingsUiState.LoginState.Loading
        )
    )

    internal val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val coroutineLoginExceptionHandler = CoroutineExceptionHandler { _, exception ->
        handleLoginError(exception)
    }

    private val coroutineSettingsExceptionHandler = CoroutineExceptionHandler { _, exception ->
        handleAppSettingsError(exception)
    }

    init {
        collectSettings()
        collectLogin()
    }

    private fun collectSettings() {
        viewModelScope.launch(backgroundDispatcher + coroutineSettingsExceptionHandler) {
            settingsHandler
                .getSettings()
                .map { settings ->
                    SettingsUiState.AppSettingsState.Loaded(settings)
                }.catch { e ->
                    handleAppSettingsError(e)
                }
                .stateIn(
                    viewModelScope,
                    SharingStarted.Eagerly,
                    SettingsUiState.AppSettingsState.Loading,
                ).collect { settingsState ->
                    _uiState.update { state ->
                        state.copy(
                            appSettingsState = settingsState,
                        )
                    }
                }
        }
    }

    private fun collectLogin() {
        viewModelScope.launch(backgroundDispatcher + coroutineLoginExceptionHandler) {
            loginRepository
                .getLoginFlow()
                .map { userData ->
                    userData?.let { SettingsUiState.LoginState.Auth(it) }
                        ?: SettingsUiState.LoginState.Unauthorized
                }
                .catch { e ->
                    handleLoginError(e)
                }
                .stateIn(
                    viewModelScope,
                    SharingStarted.Eagerly,
                    SettingsUiState.LoginState.Loading,
                )
                .collect { loginState ->
                    _uiState.update { state ->
                        state.copy(
                            loginState = loginState
                        )
                    }
                }
        }
    }

    internal fun onLogin(result: YandexAuthResult) {
        viewModelScope.launch(coroutineLoginExceptionHandler) {
            when (result) {
                is YandexAuthResult.Success -> loginRepository.registerUser(result.token.value)

                is YandexAuthResult.Failure -> handleLoginError(LoginException())

                is YandexAuthResult.Cancelled -> {}
            }
        }
    }

    internal fun onLogout() {
        viewModelScope.launch(coroutineLoginExceptionHandler) {
            loginRepository.removeLogin()
        }
    }

    internal fun onAppSettingsChange(userSettings: UserSettings) {
        viewModelScope.launch(coroutineSettingsExceptionHandler) {
            settingsHandler.saveSettings(userSettings)
        }
    }

    private fun handleLoginError(exception: Throwable) {
        _uiState.update { state ->
            state.copy(
                loginState = SettingsUiState.LoginState.Error(exception)
            )
        }
    }

    private fun handleAppSettingsError(exception: Throwable) {
        _uiState.update { state ->
            state.copy(
                appSettingsState = SettingsUiState.AppSettingsState.Error(exception)
            )
        }
    }
}