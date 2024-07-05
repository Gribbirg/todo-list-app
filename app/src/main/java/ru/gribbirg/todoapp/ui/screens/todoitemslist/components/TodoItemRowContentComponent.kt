package ru.gribbirg.todoapp.ui.screens.todoitemslist.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import ru.gribbirg.todoapp.data.data.TodoImportance
import ru.gribbirg.todoapp.data.data.TodoItem
import ru.gribbirg.todoapp.ui.previews.DefaultPreview
import ru.gribbirg.todoapp.ui.previews.FontScalePreviews
import ru.gribbirg.todoapp.ui.previews.ItemPreviewTemplate
import ru.gribbirg.todoapp.ui.previews.LayoutDirectionPreviews
import ru.gribbirg.todoapp.ui.previews.ThemePreviews
import ru.gribbirg.todoapp.ui.previews.TodoItemPreviewParameterProvider
import ru.gribbirg.todoapp.ui.theme.AppTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * Main item content for row
 *
 * @see TodoItemRow
 */
@Composable
fun TodoItemRowContent(
    item: TodoItem,
    onChecked: (Boolean) -> Unit,
    onInfoClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        ItemCheckBox(
            completed = item.completed,
            highImportance = item.importance == TodoImportance.High,
            onChecked = { value -> onChecked(value) }
        )
        Spacer(modifier = Modifier.width(AppTheme.dimensions.paddingSmall))
        if (item.importance.logoId != null) {
            ImportanceIcon(
                importance = item.importance,
                modifier = Modifier.padding(top = AppTheme.dimensions.paddingMediumLarge)
            )
            Spacer(modifier = Modifier.width(AppTheme.dimensions.paddingSmall))
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(top = AppTheme.dimensions.paddingMediumLarge)
        ) {
            ItemText(
                text = item.text,
                completed = item.completed,
                modifier = Modifier.padding(top = AppTheme.dimensions.paddingSmall)
            )
            if (item.deadline != null) {
                Spacer(modifier = Modifier.height(AppTheme.dimensions.paddingMedium))
                DeadlineText(item.deadline)
            }
        }
        InfoIconButton(
            onInfoClicked = onInfoClicked
        )
    }
}

@Composable
private fun ImportanceIcon(importance: TodoImportance, modifier: Modifier = Modifier) {
    Icon(
        painter = painterResource(id = importance.logoId!!),
        contentDescription = stringResource(id = importance.nameId),
        modifier = modifier,
        tint = importance.colorId?.let { colorResource(id = it) }
            ?: AppTheme.colors.gray
    )
}

@Composable
private fun InfoIconButton(onInfoClicked: () -> Unit) {
    IconButton(
        onClick = { onInfoClicked() },
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = AppTheme.colors.tertiary
        )
    ) {
        Icon(Icons.Outlined.Info, contentDescription = "")
    }
}

@Composable
private fun DeadlineText(deadline: LocalDate, modifier: Modifier = Modifier) {
    val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
    Text(
        text = dateFormatter.format(deadline),
        modifier = modifier,
        color = AppTheme.colors.tertiary,
        style = AppTheme.typography.subhead
    )
}

@Composable
private fun ItemText(text: String, completed: Boolean, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
        textDecoration =
        if (completed)
            TextDecoration.LineThrough
        else
            null,
        color = if (completed)
            AppTheme.colors.tertiary
        else
            AppTheme.colors.primary,
        style = AppTheme.typography.body
    )
}

@Composable
private fun ItemCheckBox(
    completed: Boolean,
    highImportance: Boolean,
    onChecked: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Checkbox(
        checked = completed,
        onCheckedChange = { onChecked(it) },
        modifier = modifier,
        colors =
        if (highImportance)
            CheckboxColors(
                checkedCheckmarkColor = AppTheme.colors.secondaryBack,
                uncheckedCheckmarkColor = Color.Unspecified,
                checkedBoxColor = AppTheme.colors.green,
                uncheckedBoxColor = AppTheme.colors.red.copy(alpha = 0.16f),
                disabledCheckedBoxColor = Color.Unspecified,
                disabledUncheckedBoxColor = Color.Unspecified,
                disabledIndeterminateBoxColor = Color.Unspecified,
                checkedBorderColor = AppTheme.colors.green,
                uncheckedBorderColor = AppTheme.colors.red,
                disabledBorderColor = Color.Unspecified,
                disabledUncheckedBorderColor = Color.Unspecified,
                disabledIndeterminateBorderColor = Color.Unspecified,
            )
        else
            CheckboxDefaults.colors(
                checkedColor = AppTheme.colors.green,
                uncheckedColor = AppTheme.colors.separator,
                checkmarkColor = AppTheme.colors.secondaryBack
            )
    )
}

@DefaultPreview
@ThemePreviews
@LayoutDirectionPreviews
@FontScalePreviews
@Composable
private fun TodoItemRowContentPreview(
    @PreviewParameter(TodoItemPreviewParameterProvider::class) item: TodoItem,
) {
    var itemState by remember {
        mutableStateOf(item)
    }
    ItemPreviewTemplate {
        TodoItemRowContent(
            item = itemState,
            onChecked = { itemState = itemState.copy(completed = it) },
            onInfoClicked = { })
    }
}