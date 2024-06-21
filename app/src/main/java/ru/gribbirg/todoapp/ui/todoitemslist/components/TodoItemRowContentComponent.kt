package ru.gribbirg.todoapp.ui.todoitemslist.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.gribbirg.todoapp.R
import ru.gribbirg.todoapp.data.data.TodoImportance
import ru.gribbirg.todoapp.data.data.TodoItem
import ru.gribbirg.todoapp.ui.theme.AppTheme
import java.time.LocalDate

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
            highImportance = item.importance == TodoImportance.HIGH,
            onChecked = { value -> onChecked(value) }
        )
        Spacer(modifier = Modifier.width(4.dp))
        if (item.importance != TodoImportance.NO) {
            ImportanceIcon(importance = item.importance, modifier = Modifier.padding(top = 12.dp))
            Spacer(modifier = Modifier.width(5.dp))
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            ItemText(
                text = item.text,
                completed = item.completed
            )
            if (item.deadline != null) {
                Spacer(modifier = Modifier.height(8.dp))
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
        contentDescription = stringResource(id = importance.resourceId),
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
    Text(
        text = stringResource(
            id = R.string.day_month_date_template,
            deadline.dayOfMonth,
            stringArrayResource(id = R.array.months_names)[deadline.monthValue - 1]
        ),
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
