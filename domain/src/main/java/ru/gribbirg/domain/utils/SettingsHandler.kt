package ru.gribbirg.domain.utils

import kotlinx.coroutines.flow.StateFlow
import ru.gribbirg.domain.model.user.UserSettings

interface SettingsHandler {
    fun getSettings(): StateFlow<UserSettings>
    suspend fun saveSettings(settings: UserSettings)
}