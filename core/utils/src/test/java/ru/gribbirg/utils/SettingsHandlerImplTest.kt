package ru.gribbirg.utils

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.gribbirg.domain.model.user.ThemeSettings
import ru.gribbirg.domain.model.user.UserSettings
import ru.gribbirg.domain.utils.KeyValueDataSaver
import ru.gribbirg.utils.SettingsHandlerImpl.Companion.SETTINGS_KEY

class SettingsHandlerImplTest {

    private val dispatcher = StandardTestDispatcher()

    private fun mockKeyValueSaver(settingsDtoString: String?): KeyValueDataSaver =
        mockk(relaxed = true) {
            coEvery { this@mockk.get(SETTINGS_KEY) } returns settingsDtoString
        }

    @Test
    fun `get settings first time`() = runTest(dispatcher) {
        val saver = mockKeyValueSaver(null)
        val settingsHandler = SettingsHandlerImpl(saver, dispatcher)

        val res = settingsHandler.getSettings().value

        assertEquals(UserSettings(), res)
    }

    @Test
    fun `get saved settings`() = runTest(dispatcher) {
        val theme = ThemeSettings.Dark
        val saver = mockKeyValueSaver("""{"theme":"${theme.name}"}""")
        val settingsHandler = SettingsHandlerImpl(saver, dispatcher)
        yield()

        val res = settingsHandler.getSettings().value

        assertEquals(UserSettings(theme = theme), res)
    }

    @Test
    fun `save settings and get`() = runTest(dispatcher) {
        val theme = ThemeSettings.Dark
        val settings = UserSettings(theme = theme)
        val saver = mockKeyValueSaver("""{"theme":"${theme.name}"}""")
        val settingsHandler = SettingsHandlerImpl(saver, dispatcher)

        settingsHandler.saveSettings(settings)
        yield()

        coVerify { saver.save(SETTINGS_KEY, """{"theme":"${theme.name}"}""") }
        assertEquals(settings, settingsHandler.getSettings().value)
    }
}