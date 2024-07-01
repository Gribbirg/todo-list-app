package ru.gribbirg.todoapp.ui.todoitemslist.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import ru.gribbirg.todoapp.ui.previews.DefaultPreview
import ru.gribbirg.todoapp.ui.previews.ItemPreviewTemplate
import ru.gribbirg.todoapp.ui.previews.ThemePreviews
import ru.gribbirg.todoapp.ui.theme.AppTheme

@Composable
fun TodoItemSwipeToDismiss(
    completed: Boolean,
    onChecked: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    animationDuration: Int = AppTheme.dimensions.animationDuration,
    dismissOnCheck: Boolean = false,
    content: @Composable (SwipeToDismissBoxState) -> Unit
) {
    var deleted by remember { mutableStateOf(false) }
    var checked by remember { mutableStateOf(false) }

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            return@rememberSwipeToDismissBoxState when (it) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    checked = true
                    false
                }

                SwipeToDismissBoxValue.EndToStart -> {
                    deleted = true
                    true
                }

                SwipeToDismissBoxValue.Settled -> false
            }
        },
        positionalThreshold = { it * .25f }
    )

    LaunchedEffect(deleted) {
        if (deleted) {
            delay(animationDuration.toLong())
            onDelete()
        }
    }

    LaunchedEffect(checked) {
        if (checked) {
            if (dismissOnCheck)
                delay(animationDuration.toLong())
            checked = false
            onChecked()
        }
    }

    AnimatedVisibility(
        visible = !(deleted || (dismissOnCheck && checked)),
        exit = fadeOut(
            tween(
                durationMillis = animationDuration
            )
        )
    ) {
        SwipeToDismissBox(
            state = dismissState,
            modifier = modifier,
            backgroundContent = {
                TodoItemRowSwipeBackground(
                    dismissState = dismissState,
                )
            },
            enableDismissFromStartToEnd = !completed
        ) {
            content(dismissState)
        }
    }
}

@DefaultPreview
@ThemePreviews
@Composable
private fun TodoItemSwipeToDismissPreview() {
    ItemPreviewTemplate {
        var completed by remember {
            mutableStateOf(false)
        }
        TodoItemSwipeToDismiss(
            completed = completed,
            onChecked = { completed = true },
            onDelete = { }) {
            Box(
                modifier = Modifier
                    .height(100.dp)
                    .width(300.dp)
                    .background(AppTheme.colors.secondaryBack)
            ) {
                Text(
                    text = "Checked: $completed",
                    textAlign = TextAlign.Center,
                    color = AppTheme.colors.secondary
                )
            }
        }
    }
}