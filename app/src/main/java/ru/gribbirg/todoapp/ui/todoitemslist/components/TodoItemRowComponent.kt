package ru.gribbirg.todoapp.ui.todoitemslist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.gribbirg.todoapp.R
import ru.gribbirg.todoapp.data.data.TodoImportance
import ru.gribbirg.todoapp.data.data.TodoItem
import ru.gribbirg.todoapp.ui.theme.AppTheme
import java.time.LocalDate

@Composable
fun TodoItemRow(
    item: TodoItem,
    onChecked: (Boolean) -> Unit,
    onDeleted: () -> Unit,
    onInfoClicked: () -> Unit,
    dismissOnCheck: Boolean = false,
    topBorderRadius: Dp = 0.dp,
) {
    val shape = RoundedCornerShape(
        topStart = topBorderRadius,
        topEnd = topBorderRadius,
    )
    BoxWithSidesForShadow(
        sides = if (topBorderRadius == 0.dp) Sides.LEFT_AND_RIGHT else Sides.TOP,
    ) {
        TodoItemSwipeToDismiss(
            completed = item.completed,
            onChecked = { onChecked(true) },
            onDelete = onDeleted,
            dismissOnCheck = dismissOnCheck,
            topBorderRadius = topBorderRadius
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(2.dp, shape)
                    .clip(shape)
                    .background(AppTheme.colors.secondaryBack)
                    .padding(
                        start = 8.dp,
                        end = 8.dp,
                        top = 4.dp
                    ),
            ) {
                ItemCheckBox(
                    completed = item.completed,
                    highImportance = item.importance == TodoImportance.HIGH,
                    onChecked = { value -> onChecked(value) }
                )
                Spacer(modifier = Modifier.width(4.dp))
                if (item.importance != TodoImportance.NO) {
                    Icon(
                        painter = painterResource(id = item.importance.logoId!!),
                        contentDescription = stringResource(id = item.importance.resourceId),
                        modifier = Modifier
                            .padding(top = 12.dp),
                        tint = item.importance.colorId?.let { colorResource(id = it) }
                            ?: AppTheme.colors.gray
                    )
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
                IconButton(
                    onClick = { onInfoClicked() },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = AppTheme.colors.tertiary
                    )
                ) {
                    Icon(Icons.Outlined.Info, contentDescription = "")
                }
            }
        }
    }
}

@Composable
private fun DeadlineText(deadline: LocalDate) {
    Text(
        text = stringResource(
            id = R.string.day_month_date_template,
            deadline.dayOfMonth,
            stringArrayResource(id = R.array.months_names)[deadline.monthValue - 1]
        ),
        color = AppTheme.colors.tertiary,
        style = AppTheme.typography.subhead
    )
}

@Composable
private fun ItemText(text: String, completed: Boolean) {
    Text(
        text = text,
        modifier = Modifier,
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
) {
    Checkbox(
        checked = completed,
        onCheckedChange = { onChecked(it) },
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

@Composable
internal fun BoxWithSidesForShadow(
    sides: Sides,
    content: @Composable () -> Unit
) {
    val shadowShape =
        when (sides) {
            Sides.LEFT_AND_RIGHT, Sides.BOTTOM -> GenericShape { size, _ ->
                val maxSize = (size.width + size.height) * 10
                moveTo(-maxSize, 0f)
                lineTo(maxSize, 0f)
                lineTo(maxSize, size.height + maxSize)
                lineTo(0f, size.height + maxSize)
            }

            Sides.TOP -> GenericShape { size, _ ->
                val maxSize = (size.width + size.height) * 10
                moveTo(-maxSize, -maxSize)
                lineTo(maxSize, -maxSize)
                lineTo(maxSize, size.height + maxSize)
                lineTo(0f, size.height + maxSize)
            }
        }

    Box(
        modifier = Modifier
            .clip(shadowShape)
    ) {
        content()
    }
}

internal enum class Sides {
    LEFT_AND_RIGHT,
    BOTTOM,
    TOP
}