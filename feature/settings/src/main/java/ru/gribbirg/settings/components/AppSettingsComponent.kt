package ru.gribbirg.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.gribbirg.domain.model.user.UserSettings
import ru.gribbirg.settings.SettingsUiState
import ru.gribbirg.ui.components.ErrorComponent
import ru.gribbirg.ui.components.LoadingComponent

@Composable
internal fun AppSettingsContent(
    uiState: SettingsUiState.AppSettingsState,
    onChange: (UserSettings) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        when (uiState) {
            is SettingsUiState.AppSettingsState.Loaded -> {
                ThemeSelectorRow(
                    selected = uiState.settings.theme,
                    onSelect = { onChange(uiState.settings.copy(theme = it)) },
                )
            }

            is SettingsUiState.AppSettingsState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    LoadingComponent()
                }
            }

            is SettingsUiState.AppSettingsState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    ErrorComponent(exception = uiState.exception)
                }
            }
        }
    }
}