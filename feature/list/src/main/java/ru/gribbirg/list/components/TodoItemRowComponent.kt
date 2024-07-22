package ru.gribbirg.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.PreviewParameter
import ru.gribbirg.domain.model.todo.TodoItem
import ru.gribbirg.theme.custom.AppTheme
import ru.gribbirg.todoapp.list.R
import ru.gribbirg.ui.previews.DefaultPreview
import ru.gribbirg.ui.previews.FontScalePreviews
import ru.gribbirg.ui.previews.ItemPreviewTemplate
import ru.gribbirg.ui.previews.LayoutDirectionPreviews
import ru.gribbirg.ui.previews.ThemePreviews
import ru.gribbirg.ui.previews.TodoItemPreviewParameterProvider

/**
 * Main row component
 */
@Composable
internal fun TodoItemRow(
    item: TodoItem,
    onChecked: (Boolean) -> Unit,
    onDeleted: () -> Unit,
    onInfoClicked: () -> Unit,
    modifier: Modifier = Modifier,
    dismissOnCheck: Boolean = false,
) {
    val context = LocalContext.current

    BoxWithSidesForShadow(
        sides = Sides.LEFT_AND_RIGHT,
        modifier = modifier,
    ) {
        TodoItemSwipeToDismiss(
            completed = item.completed,
            onChecked = { onChecked(true) },
            onDelete = onDeleted,
            dismissOnCheck = dismissOnCheck,
            modifier = Modifier
                .semantics(mergeDescendants = true) {
                    stateDescription = if (item.completed)
                        context.getString(R.string.done)
                    else
                        context.getString(R.string.done_not)

                    this[SemanticsActions.CustomActions] = listOf(
                        if (item.completed) {
                            CustomAccessibilityAction(context.getString(R.string.perform)) {
                                onChecked(true)
                                true
                            }
                        } else {
                            CustomAccessibilityAction(context.getString(R.string.fail)) {
                                onChecked(false)
                                true
                            }
                        },
                        CustomAccessibilityAction(context.getString(R.string.edit)) {
                            onInfoClicked()
                            true
                        },
                        CustomAccessibilityAction(context.getString(R.string.delete)) {
                            onDeleted()
                            true
                        }
                    )
                }
        ) {
            TodoItemRowContent(
                item = item,
                onChecked = onChecked,
                onInfoClicked = onInfoClicked,
                modifier = Modifier
                    .defaultMinSize(minHeight = AppTheme.dimensions.sizeItemMinHeightMedium)
                    .fillMaxWidth()
                    .shadow(AppTheme.dimensions.shadowElevationSmall)
                    .background(AppTheme.colors.secondaryBack)
                    .padding(
                        start = AppTheme.dimensions.paddingMedium,
                        end = AppTheme.dimensions.paddingMedium,
                        bottom = AppTheme.dimensions.paddingSmall
                    ),
            )
        }
    }
}


@Composable
internal fun BoxWithSidesForShadow(
    sides: Sides,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
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

            Sides.ALL -> null
        }

    Box(
        modifier = shadowShape?.let { modifier.clip(shadowShape) } ?: modifier
    ) {
        content()
    }
}


internal enum class Sides {
    LEFT_AND_RIGHT,
    BOTTOM,
    TOP,
    ALL,
    ;
}

@DefaultPreview
@ThemePreviews
@LayoutDirectionPreviews
@FontScalePreviews
@Composable
private fun TodoItemRowPreview(
    @PreviewParameter(TodoItemPreviewParameterProvider::class) item: TodoItem
) {
    var itemState by remember {
        mutableStateOf(item)
    }
    ItemPreviewTemplate {
        TodoItemRow(
            item = itemState,
            onChecked = { itemState = item.copy(completed = it) },
            onDeleted = { },
            onInfoClicked = { })
    }
}