package ru.gribbirg.edit.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import ru.gribbirg.theme.custom.AppTheme
import ru.gribbirg.todoapp.edit.R
import ru.gribbirg.ui.previews.DefaultPreview
import ru.gribbirg.ui.previews.ItemPreviewTemplate
import ru.gribbirg.ui.previews.LanguagePreviews
import ru.gribbirg.ui.previews.LayoutDirectionPreviews
import ru.gribbirg.ui.previews.ThemePreviews
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * Deadline part of edit screen
 *
 * @see ItemDeadlineDatePicker
 */
@Composable
internal fun ItemDeadline(
    deadline: LocalDate?,
    onChanged: (LocalDate?) -> Unit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    var dialogOpened by remember {
        mutableStateOf(false)
    }
    val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = stringResource(id = R.string.deadline),
                modifier = Modifier.padding(start = AppTheme.dimensions.paddingMedium),
                style = AppTheme.typography.body,
                color = AppTheme.colors.primary
            )
            TextButton(
                onClick = {
                    onClick()
                    dialogOpened = true
                },
                modifier = Modifier.clearAndSetSemantics {
                    if (deadline != null) {
                        contentDescription = dateFormatter.format(deadline)
                    }
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = AppTheme.colors.blue
                )
            ) {
                if (deadline != null) {
                    Text(
                        text = dateFormatter.format(deadline),
                        style = AppTheme.typography.subhead
                    )
                }
            }
        }
        Switch(
            checked = deadline != null,
            onCheckedChange = {
                onClick()
                onChanged(if (it) LocalDate.now() else null)
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = AppTheme.colors.white,
                checkedTrackColor = AppTheme.colors.blue,
                checkedBorderColor = Color.Transparent,
                uncheckedThumbColor = AppTheme.colors.separator,
                uncheckedTrackColor = AppTheme.colors.secondaryBack,
                uncheckedBorderColor = AppTheme.colors.separator
            )
        )
    }

    if (dialogOpened) {
        ItemDeadlineDatePicker(
            startingValue = deadline!!,
            onChanged = onChanged,
            close = { dialogOpened = false })
    }
}

@DefaultPreview
@ThemePreviews
@LanguagePreviews
@LayoutDirectionPreviews
@Composable
private fun DeadlineComponentPreview() {
    ItemPreviewTemplate {
        var deadline by remember { mutableStateOf(LocalDate.now()) }
        ItemDeadline(
            deadline = deadline,
            onChanged = { deadline = if (deadline == null) LocalDate.now() else null },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
