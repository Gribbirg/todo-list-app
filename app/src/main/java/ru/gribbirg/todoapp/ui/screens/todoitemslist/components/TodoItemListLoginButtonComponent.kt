package ru.gribbirg.todoapp.ui.screens.todoitemslist.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthResult
import com.yandex.authsdk.YandexAuthSdk
import ru.gribbirg.todoapp.R
import ru.gribbirg.todoapp.ui.theme.AppTheme

/**
 * Login button
 */
@Composable
fun TodoItemListLoginButtonComponent(
    isLogin: Boolean?,
    onLogin: (YandexAuthResult) -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier,
    loginOptions: YandexAuthLoginOptions = YandexAuthLoginOptions(),
) {
    val sdk = YandexAuthSdk.create(YandexAuthOptions(LocalContext.current))
    val launcher = rememberLauncherForActivityResult(sdk.contract) { result -> onLogin(result) }

    IconButton(
        onClick = {
            if (isLogin == true)
                onExit()
            else
                launcher.launch(loginOptions)
        },
        modifier = modifier,
        enabled = isLogin != null,
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = AppTheme.colors.blue,
        ),
    ) {
        if (isLogin == true)
            Icon(
                Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = stringResource(R.string.exit_from_acc)
            )
        else
            Icon(
                Icons.Default.AccountCircle,
                contentDescription = stringResource(R.string.login)
            )
    }
}