package ru.gribbirg.settings

import ru.gribbirg.domain.model.user.UserData
import ru.gribbirg.domain.model.user.UserSettings

internal data class SettingsUiState(
    val appSettingsState: AppSettingsState,
    val loginState: LoginState,
) {
    sealed class AppSettingsState {
        data object Loading : AppSettingsState()
        data class Loaded(val settings: UserSettings) : AppSettingsState()
        data class Error(val exception: Throwable) : AppSettingsState()
    }

    sealed class LoginState {
        data object Loading : LoginState()
        data class Auth(val user: UserData) : LoginState()
        data object Unauthorized : LoginState()
        data class Error(val exception: Throwable) : LoginState()
    }
}