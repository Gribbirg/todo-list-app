package ru.gribbirg.list.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import ru.gribbirg.theme.custom.AppTheme
import ru.gribbirg.todoapp.list.R
import ru.gribbirg.ui.previews.DefaultPreview
import ru.gribbirg.ui.previews.LanguagePreviews
import ru.gribbirg.ui.previews.LayoutDirectionPreviews
import ru.gribbirg.ui.previews.ScreenPreviewTemplate
import ru.gribbirg.ui.previews.ThemePreviews

@Composable
internal fun MenuIconButton(
    toAbout: () -> Unit,
    toSettings: () -> Unit,
) {
    var menuOpened by remember { mutableStateOf(false) }

    IconButton(onClick = { menuOpened = true }) {
        Icon(
            Icons.Default.MoreVert,
            contentDescription = stringResource(id = R.string.menu),
            tint = AppTheme.colors.blue
        )

        DropdownMenu(
            expanded = menuOpened,
            onDismissRequest = { menuOpened = false },
            containerColor = AppTheme.colors.elevated,
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(id = R.string.settings),
                        style = AppTheme.typography.body,
                        color = AppTheme.colors.primary,
                    )
                },
                onClick = toSettings,
                leadingIcon = {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = null,
                        tint = AppTheme.colors.primary
                    )
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(id = R.string.about_app),
                        style = AppTheme.typography.body,
                        color = AppTheme.colors.primary,
                    )
                },
                onClick = toAbout,
                leadingIcon = {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        tint = AppTheme.colors.primary,
                    )
                }
            )
        }
    }
}

@DefaultPreview
@ThemePreviews
@LanguagePreviews
@LayoutDirectionPreviews
@Composable
private fun MenuIconButtonPreview() {
    ScreenPreviewTemplate {
        MenuIconButton(toAbout = {}, toSettings = {})
    }
}