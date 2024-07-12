package ru.gribbirg.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.gribbirg.todoapp.ui.R
import ru.gribbirg.ui.previews.DefaultPreview
import ru.gribbirg.ui.previews.ItemPreviewTemplate
import ru.gribbirg.ui.previews.LanguagePreviews
import ru.gribbirg.ui.previews.ThemePreviews
import ru.gribbirg.ui.theme.AppTheme

/**
 * Base error component
 */
@Composable
fun ErrorComponent(exception: Throwable, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.error_occurred),
            modifier = Modifier.padding(4.dp),
            color = AppTheme.colors.red,
            style = AppTheme.typography.body,
        )
        Text(
            text = "${exception::class.simpleName}:",
            modifier = Modifier.padding(4.dp),
            color = AppTheme.colors.red,
            style = AppTheme.typography.body,
        )
        Text(
            text = exception.localizedMessage ?: exception.message
            ?: stringResource(R.string.unknown_error),
            modifier = Modifier.padding(4.dp),
            color = AppTheme.colors.red,
            style = AppTheme.typography.body,
        )
    }
}

@DefaultPreview
@ThemePreviews
@LanguagePreviews
@Composable
private fun ErrorComponentPreview() {
    ItemPreviewTemplate {
        ErrorComponent(exception = Exception())
    }
}