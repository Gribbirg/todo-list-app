package ru.gribbirg.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ru.gribbirg.theme.custom.AppTheme

/**
 * Background for swipe to dismiss
 */
@Composable
internal fun TodoItemRowSwipeBackground(dismissState: SwipeToDismissBoxState) {
    val color = when (dismissState.dismissDirection) {
        SwipeToDismissBoxValue.StartToEnd -> AppTheme.colors.green
        SwipeToDismissBoxValue.EndToStart -> AppTheme.colors.red
        SwipeToDismissBoxValue.Settled -> Color.Transparent
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(
                horizontal = AppTheme.dimensions.paddingMediumLarge,
                vertical = AppTheme.dimensions.paddingMedium
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        if (dismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd)
            Icon(Icons.Filled.Check, contentDescription = null, tint = AppTheme.colors.white)
        Spacer(modifier = Modifier)
        if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart)
            Icon(Icons.Filled.Delete, contentDescription = null, tint = AppTheme.colors.white)
    }
}