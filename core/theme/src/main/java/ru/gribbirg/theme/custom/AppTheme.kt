package ru.gribbirg.theme.custom

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import ru.gribbirg.domain.model.user.ThemeSettings
import ru.gribbirg.theme.material.TodoAppMaterialTheme

/**
 * Main app theme
 *
 * @see AppColors
 * @see AppTheme
 * @see AppDimensions
 */
object AppTheme {
    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    val typography: AppTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current

    val dimensions: AppDimensions
        @Composable
        @ReadOnlyComposable
        get() = LocalDimensions.current
}

@Composable
fun TodoAppTheme(
    colors: AppColors? = null,
    typography: AppTypography = AppTheme.typography,
    dimensions: AppDimensions = AppTheme.dimensions,
    theme: ThemeSettings = ThemeSettings.LikeSystem,
    content: @Composable () -> Unit
) {
    val darkTheme = when (theme) {
        ThemeSettings.LikeSystem -> isSystemInDarkTheme()
        ThemeSettings.Light -> false
        ThemeSettings.Dark -> true
    }

    val appColors =
        colors ?: if (darkTheme) AppColors.darkColors() else AppColors.lightColors()
    val rememberedColors = remember { appColors.copy() }.apply { updateColorsFrom(appColors) }

    CompositionLocalProvider(
        LocalColors provides rememberedColors,
        LocalDimensions provides dimensions,
        LocalTypography provides typography
    ) {
        TodoAppMaterialTheme(
            darkTheme = darkTheme,
        ) {
            content()
        }
    }
}