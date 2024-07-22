package ru.gribbirg.settings.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthResult
import com.yandex.authsdk.YandexAuthSdk
import ru.gribbirg.domain.model.user.UserData
import ru.gribbirg.settings.SettingsUiState
import ru.gribbirg.theme.custom.AppTheme
import ru.gribbirg.todoapp.settings.R
import ru.gribbirg.ui.components.ErrorComponent
import ru.gribbirg.ui.components.LoadingComponent
import ru.gribbirg.ui.previews.DefaultPreview
import ru.gribbirg.ui.previews.ItemPreviewTemplate
import ru.gribbirg.ui.previews.LanguagePreviews
import ru.gribbirg.ui.previews.LayoutDirectionPreviews
import ru.gribbirg.ui.previews.ThemePreviews

@Composable
internal fun LoginRow(
    uiState: SettingsUiState.LoginState,
    onLogin: (YandexAuthResult) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    loginOptions: YandexAuthLoginOptions = YandexAuthLoginOptions(),
) {

    Box(modifier = modifier) {
        when (uiState) {
            is SettingsUiState.LoginState.Loading -> {
                LoadingContent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = AppTheme.dimensions.sizeItemMinHeightLarge),
                )
            }

            is SettingsUiState.LoginState.Auth -> {
                UserContent(
                    user = uiState.user,
                    onLogout = onLogout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppTheme.dimensions.paddingMedium)
                )
            }

            is SettingsUiState.LoginState.Unauthorized -> {
                val sdk = YandexAuthSdk.create(YandexAuthOptions(LocalContext.current))
                val launcher =
                    rememberLauncherForActivityResult(sdk.contract) { result -> onLogin(result) }
                UnauthorizedContent(
                    onClick = {
                        launcher.launch(loginOptions)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = AppTheme.dimensions.sizeItemMinHeightLarge),
                )
            }

            is SettingsUiState.LoginState.Error -> {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    ErrorComponent(exception = uiState.exception)
                }
            }
        }
    }
}

@Composable
private fun LoadingContent(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        LoadingComponent()
    }
}

@Composable
private fun UnauthorizedContent(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painterResource(id = R.drawable.baseline_login_24),
            contentDescription = stringResource(id = R.string.login_to_acc),
            tint = AppTheme.colors.blue
        )
        Spacer(modifier = Modifier.width(AppTheme.dimensions.paddingMedium))
        Text(
            text = stringResource(id = R.string.login_to_acc),
            style = AppTheme.typography.title,
            color = AppTheme.colors.blue
        )
    }
}

@Composable
private fun UserContent(
    user: UserData,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = user.name,
            modifier = Modifier.padding(start = AppTheme.dimensions.paddingMedium),
            style = AppTheme.typography.title,
            color = AppTheme.colors.primary,
        )
        IconButton(onClick = onLogout) {
            Icon(
                painterResource(id = R.drawable.baseline_logout_24),
                contentDescription = stringResource(id = R.string.logout_of_acc),
                tint = AppTheme.colors.blue,
            )
        }
    }
}

@DefaultPreview
@ThemePreviews
@LanguagePreviews
@LayoutDirectionPreviews
@Composable
private fun LoginRowPreview() {
    ItemPreviewTemplate {
        Column {
            LoadingContent(
                Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = AppTheme.dimensions.sizeItemMinHeightLarge),
            )
            HorizontalDivider()
            UnauthorizedContent(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = AppTheme.dimensions.sizeItemMinHeightLarge),
            )
            HorizontalDivider()
            UserContent(
                user = UserData("Alex"), onLogout = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = AppTheme.dimensions.sizeItemMinHeightLarge),
            )
        }
    }
}