package ru.gribbirg.todoapp.ui.todoitemslist

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

@Composable
fun TodoItemRow(
    item: TodoItem,
    onChecked: (TodoItem, Boolean) -> Unit,
    onInfoClicked: (TodoItem) -> Unit,
    topBorderRadius: Dp = 0.dp,
) {
    val shape = RoundedCornerShape(
        topStart = topBorderRadius,
        topEnd = topBorderRadius,
    )
    BoxWithSidesForShadow(
        sides = if (topBorderRadius == 0.dp) Sides.LEFT_AND_RIGHT else Sides.TOP,
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
            Checkbox(
                checked = item.completed,
                onCheckedChange = { onChecked(item, it) },
                colors =
                if (item.importance == TodoImportance.HIGH)
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
                        uncheckedColor = AppTheme.colors.tertiary,
                        checkmarkColor = AppTheme.colors.secondaryBack
                    )
            )
            Spacer(modifier = Modifier.width(4.dp))
            if (item.importance == TodoImportance.HIGH) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_priority_high_24),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 12.dp),
                    tint = colorResource(id = TodoImportance.HIGH.colorId!!)
                )
                Spacer(modifier = Modifier.width(5.dp))
            } else if (item.importance == TodoImportance.LOW) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_south_24),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 12.dp),
                    tint = AppTheme.colors.tertiary
                )
                Spacer(modifier = Modifier.width(5.dp))
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text(
                    text = item.text,
                    modifier = Modifier,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    textDecoration =
                    if (item.completed)
                        TextDecoration.LineThrough
                    else
                        null,
                    color = if (item.completed)
                        AppTheme.colors.tertiary
                    else
                        AppTheme.colors.primary
                )
                if (item.deadline != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(
                            id = R.string.day_month_date_template,
                            item.deadline.dayOfMonth,
                            stringArrayResource(id = R.array.months_names)[item.deadline.monthValue - 1]
                        ),
                        color = AppTheme.colors.tertiary
                    )
                }
            }
            IconButton(
                onClick = { onInfoClicked(item) },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = AppTheme.colors.tertiary
                )
            ) {
                Icon(Icons.Outlined.Info, contentDescription = "")
            }
        }
    }
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