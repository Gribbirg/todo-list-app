package ru.gribbirg.todoapp.ui.edititem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ru.gribbirg.todoapp.R
import ru.gribbirg.todoapp.data.data.TodoImportance
import ru.gribbirg.todoapp.ui.previews.DefaultPreview
import ru.gribbirg.todoapp.ui.previews.LanguagePreviews
import ru.gribbirg.todoapp.ui.previews.LayoutDirectionPreviews
import ru.gribbirg.todoapp.ui.previews.ScreenPreviewTemplate
import ru.gribbirg.todoapp.ui.previews.ThemePreviews
import ru.gribbirg.todoapp.ui.theme.AppTheme

@Composable
internal fun ItemImportanceSelector(
    importance: TodoImportance,
    onChanged: (TodoImportance) -> Unit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    var menuOpened by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(id = R.string.importance),
            modifier = Modifier.padding(
                horizontal = AppTheme.dimensions.paddingMedium,
                vertical = AppTheme.dimensions.paddingSmall
            ),
            style = AppTheme.typography.body,
            color = AppTheme.colors.primary
        )
        TextButton(
            onClick = {
                onClick()
                menuOpened = true
            },
            colors = ButtonDefaults.textButtonColors(
                contentColor = importance.colorId?.let { colorResource(it) }
                    ?: AppTheme.colors.tertiary
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (importance.logoId != null) {
                    Icon(
                        painter = painterResource(id = importance.logoId),
                        contentDescription = stringResource(id = importance.nameId),
                        modifier = Modifier,
                    )
                    Spacer(modifier = Modifier.width(AppTheme.dimensions.paddingMedium))
                }
                Text(
                    text = stringResource(id = importance.nameId),
                    style = AppTheme.typography.subhead
                )
            }
        }

        ItemImportanceMenu(
            menuOpened = menuOpened,
            onChanged = onChanged,
            onClose = { menuOpened = false }
        )
    }
}

@Composable
private fun ItemImportanceMenu(
    menuOpened: Boolean,
    onChanged: (TodoImportance) -> Unit,
    onClose: () -> Unit,
) {
    DropdownMenu(
        expanded = menuOpened,
        onDismissRequest = onClose,
        modifier = Modifier.background(AppTheme.colors.elevated)
    ) {
        for (importanceValue in TodoImportance.entries) {
            val color = importanceValue.colorId?.let { colorResource(it) }
                ?: AppTheme.colors.primary
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(id = importanceValue.nameId),
                        style = AppTheme.typography.body
                    )
                },
                onClick = {
                    onChanged(importanceValue)
                    onClose()
                },
                leadingIcon = {
                    importanceValue.logoId?.let {
                        Icon(
                            painterResource(id = it),
                            contentDescription = stringResource(id = importanceValue.nameId)
                        )
                    } ?: Spacer(modifier = Modifier)
                },
                colors = MenuDefaults.itemColors(
                    textColor = color,
                    leadingIconColor = color
                )
            )
        }
    }
}

@DefaultPreview
@ThemePreviews
@LanguagePreviews
@LayoutDirectionPreviews
@Composable
private fun ItemImportanceSelectorPreview() {
    ScreenPreviewTemplate { paddingValue ->
        Row(
            modifier = Modifier
                .background(AppTheme.colors.primaryBack)
                .padding(paddingValue),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            var importance by remember { mutableStateOf(TodoImportance.NO) }
            ItemImportanceSelector(importance = importance, onChanged = { importance = it })
        }
    }
}