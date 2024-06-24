package ru.gribbirg.todoapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.gribbirg.todoapp.R
import ru.gribbirg.todoapp.ui.theme.AppTheme

@Composable
fun ErrorComponent(exception: Throwable, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.error_occurred),
            color = AppTheme.colors.red,
            style = AppTheme.typography.body,
        )
        Text(
            text = exception.localizedMessage ?: exception.message ?: stringResource(R.string.unknown_error),
            color = AppTheme.colors.red,
            style = AppTheme.typography.body,
        )
    }
}