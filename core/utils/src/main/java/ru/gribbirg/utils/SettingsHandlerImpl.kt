package ru.gribbirg.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.gribbirg.domain.model.user.ThemeSettings
import ru.gribbirg.domain.model.user.UserSettings
import ru.gribbirg.domain.utils.KeyValueDataSaver
import ru.gribbirg.domain.utils.SettingsHandler
import ru.gribbirg.utils.di.modules.AppSettingsQualifier
import ru.gribbirg.utils.di.modules.AppSettingsScope
import javax.inject.Inject

@AppSettingsScope
internal class SettingsHandlerImpl @Inject constructor(
    @AppSettingsQualifier private val saver: KeyValueDataSaver,
    @AppSettingsQualifier private val coroutineDispatcher: CoroutineDispatcher,
) : SettingsHandler {
    private val _state = MutableStateFlow(UserSettings())

    init {
        CoroutineScope(coroutineDispatcher).launch {
            _state.update { state ->
                saver.get(SETTINGS_KEY)
                    ?.let { Json.decodeFromString<UserSettingsDto>(it) }
                    ?.toUserSettings() ?: state
            }
        }
    }

    override suspend fun saveSettings(settings: UserSettings) = withContext(coroutineDispatcher) {
        _state.update { settings }
        saver.save(SETTINGS_KEY, Json.encodeToString(settings.toDto()))
    }

    override fun getSettings(): StateFlow<UserSettings> = _state.asStateFlow()

    companion object {
        const val SETTINGS_KEY = "settings"
    }
}

@Serializable
private data class UserSettingsDto(
    val theme: String
)

private fun UserSettingsDto.toUserSettings() = UserSettings(theme = ThemeSettings.valueOf(theme))

private fun UserSettings.toDto() = UserSettingsDto(theme = theme.name)
