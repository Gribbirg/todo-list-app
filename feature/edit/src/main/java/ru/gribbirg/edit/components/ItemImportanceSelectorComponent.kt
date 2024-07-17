package ru.gribbirg.edit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import ru.gribbirg.domain.model.todo.TodoImportance
import ru.gribbirg.theme.AppTheme
import ru.gribbirg.todoapp.edit.R
import ru.gribbirg.ui.previews.DefaultPreview
import ru.gribbirg.ui.previews.LanguagePreviews
import ru.gribbirg.ui.previews.LayoutDirectionPreviews
import ru.gribbirg.ui.previews.ScreenPreviewTemplate
import ru.gribbirg.ui.previews.ThemePreviews

/**
 * Importance selector row
 */
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
                        painter = painterResource(id = importance.logoId!!),
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

        if (menuOpened) {
            ItemImportanceMenu(
                onChanged = onChanged,
                onClose = { menuOpened = false }
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
            var importance by remember { mutableStateOf(TodoImportance.No) }
            ItemImportanceSelector(importance = importance, onChanged = { importance = it })
        }
    }
}