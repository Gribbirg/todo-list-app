package ru.gribbirg.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.yandex.authsdk.YandexAuthResult
import ru.gribbirg.domain.model.user.ThemeSettings
import ru.gribbirg.domain.model.user.UserData
import ru.gribbirg.domain.model.user.UserSettings
import ru.gribbirg.settings.components.AppSettingsContent
import ru.gribbirg.settings.components.LoginRow
import ru.gribbirg.theme.custom.AppTheme
import ru.gribbirg.ui.components.CloseButton
import ru.gribbirg.ui.previews.DefaultPreview
import ru.gribbirg.ui.previews.LanguagePreviews
import ru.gribbirg.ui.previews.LayoutDirectionPreviews
import ru.gribbirg.ui.previews.OrientationPreviews
import ru.gribbirg.ui.previews.ScreenPreviewTemplate
import ru.gribbirg.ui.previews.ThemePreviews

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    SettingsScreenContent(
        uiState = uiState,
        onBack = onBack,
        onLogin = viewModel::onLogin,
        onLogout = viewModel::onLogout,
        onAppSettingsChange = viewModel::onAppSettingsChange,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreenContent(
    uiState: SettingsUiState,
    onBack: () -> Unit,
    onLogin: (YandexAuthResult) -> Unit,
    onLogout: () -> Unit,
    onAppSettingsChange: (UserSettings) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    CloseButton(onClick = onBack)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppTheme.colors.primaryBack,
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(
                    start = AppTheme.dimensions.paddingSmall,
                    end = AppTheme.dimensions.paddingSmall,
                    top = paddingValues.calculateTopPadding(),
                )
        ) {
            LoginRow(
                uiState = uiState.loginState,
                onLogin = onLogin,
                onLogout = onLogout,
                modifier = Modifier.fillMaxWidth()
            )
            HorizontalDivider(color = AppTheme.colors.grayLight)
            AppSettingsContent(
                uiState = uiState.appSettingsState,
                onChange = onAppSettingsChange,
            )
            Spacer(modifier = Modifier.height(paddingValues.calculateBottomPadding()))
        }
    }
}

@DefaultPreview
@ThemePreviews
@LanguagePreviews
@LayoutDirectionPreviews
@OrientationPreviews
@Composable
private fun SettingsScreenPreview(
    @PreviewParameter(provider = SettingsScreenUiStatePreviewParameterProvider::class)
    state: SettingsUiState,
) {
    ScreenPreviewTemplate {
        SettingsScreenContent(
            uiState = state,
            onBack = { },
            onLogin = { },
            onLogout = { },
            onAppSettingsChange = { },
        )
    }
}

internal class SettingsScreenUiStatePreviewParameterProvider :
    PreviewParameterProvider<SettingsUiState> {
    override val values: Sequence<SettingsUiState>
        get() = sequenceOf(
            SettingsUiState(
                appSettingsState = SettingsUiState.AppSettingsState.Loading,
                loginState = SettingsUiState.LoginState.Loading,
            ),
            SettingsUiState(
                appSettingsState = SettingsUiState.AppSettingsState.Loaded(
                    UserSettings(
                        ThemeSettings.Light
                    )
                ),
                loginState = SettingsUiState.LoginState.Auth(UserData("Alex")),
            ),
            SettingsUiState(
                appSettingsState = SettingsUiState.AppSettingsState.Error(Exception()),
                loginState = SettingsUiState.LoginState.Error(Exception()),
            ),
        )
}