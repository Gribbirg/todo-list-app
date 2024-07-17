package ru.gribbirg.edit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
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
import ru.gribbirg.theme.custom.AppTheme
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

    val contentColor = importance.colorId?.let { colorResource(it) }
        ?: AppTheme.colors.tertiary

    Column(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
            ) {
                onClick()
                menuOpened = true
            }
            .padding(AppTheme.dimensions.paddingSmall)
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
        Row(
            modifier = Modifier.padding(
                horizontal = AppTheme.dimensions.paddingMedium,
                vertical = AppTheme.dimensions.paddingSmall
            ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (importance.logoId != null) {
                Icon(
                    painter = painterResource(id = importance.logoId!!),
                    contentDescription = stringResource(id = importance.nameId),
                    modifier = Modifier,
                    tint = contentColor,
                )
                Spacer(modifier = Modifier.width(AppTheme.dimensions.paddingMedium))
            }
            Text(
                text = stringResource(id = importance.nameId),
                style = AppTheme.typography.subhead,
                color = importance.colorId?.let { colorResource(it) }
                    ?: AppTheme.colors.tertiary,
            )
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
            var importance by remember { mutableStateOf(TodoImportance.Low) }
            ItemImportanceSelector(importance = importance, onChanged = { importance = it })
        }
    }
}