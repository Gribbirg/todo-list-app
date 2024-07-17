package ru.gribbirg.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.gribbirg.theme.custom.AppTheme
import ru.gribbirg.todoapp.ui.R

@Composable
fun CloseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            Icons.Filled.Close,
            contentDescription = stringResource(id = R.string.close),
            tint = AppTheme.colors.primary,
        )
    }
}