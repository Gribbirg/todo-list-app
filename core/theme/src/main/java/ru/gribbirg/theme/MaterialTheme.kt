package ru.gribbirg.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun TodoAppMaterialTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) darkColorSchemeImpl else lightColorSchemeImpl

    MaterialTheme(
        colorScheme = colors,
        typography = typography,
    ) {
        content()
    }
}