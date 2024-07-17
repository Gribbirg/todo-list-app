package ru.gribbirg.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.gribbirg.theme.custom.AppTheme
import ru.gribbirg.ui.previews.DefaultPreview
import ru.gribbirg.ui.previews.ItemPreviewTemplate
import ru.gribbirg.ui.previews.ThemePreviews

/**
 * Base loading component
 */
@Composable
fun LoadingComponent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .padding(16.dp),
            color = AppTheme.colors.blue
        )
    }
}

@DefaultPreview
@ThemePreviews
@Composable
private fun LoadingComponentPreview() {
    ItemPreviewTemplate {
        LoadingComponent()
    }
}