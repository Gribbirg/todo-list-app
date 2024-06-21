package ru.gribbirg.todoapp.ui.todoitemslist.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

@Composable
fun TodoItemSwipeToDismiss(
    completed: Boolean,
    onChecked: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    animationDuration: Int = 500,
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
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
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