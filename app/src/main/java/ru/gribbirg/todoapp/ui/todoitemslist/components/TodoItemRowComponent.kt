package ru.gribbirg.todoapp.ui.todoitemslist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import ru.gribbirg.todoapp.data.data.TodoItem
import ru.gribbirg.todoapp.ui.theme.AppTheme

@Composable
fun TodoItemRow(
    item: TodoItem,
    onChecked: (Boolean) -> Unit,
    onDeleted: () -> Unit,
    onInfoClicked: () -> Unit,
    dismissOnCheck: Boolean = false,
) {
    BoxWithSidesForShadow(
        sides = Sides.LEFT_AND_RIGHT,
    ) {
        TodoItemSwipeToDismiss(
            completed = item.completed,
            onChecked = { onChecked(true) },
            onDelete = onDeleted,
            dismissOnCheck = dismissOnCheck,
        ) {
            TodoItemRowContent(
                item = item,
                onChecked = onChecked,
                onInfoClicked = onInfoClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(2.dp)
                    .background(AppTheme.colors.secondaryBack)
                    .padding(
                        start = 8.dp,
                        end = 8.dp,
                        top = 0.dp,
                        bottom = 4.dp
                    ),
            )
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