package ru.gribbirg.ui.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.gribbirg.theme.custom.AppTheme
import ru.gribbirg.theme.custom.TodoAppTheme

/**
 * Templates for previews
 */
@Composable
fun ItemPreviewTemplate(content: @Composable () -> Unit) {
    TodoAppTheme {
        Box(
            modifier = Modifier
                .background(AppTheme.colors.primaryBack)
                .padding(8.dp)
        ) {
            content()
        }
    }
}


@Composable
fun ScreenPreviewTemplate(content: @Composable (PaddingValues) -> Unit) {
    TodoAppTheme {
        Scaffold(
            containerColor = AppTheme.colors.primaryBack,
        ) {
            content(it)
        }
    }
}