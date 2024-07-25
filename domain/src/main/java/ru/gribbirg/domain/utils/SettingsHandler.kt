package ru.gribbirg.domain.utils

import kotlinx.coroutines.flow.Flow
import ru.gribbirg.domain.model.user.UserSettings

interface SettingsHandler {
    fun getSettings(): Flow<UserSettings>
    suspend fun saveSettings(settings: UserSettings)
}