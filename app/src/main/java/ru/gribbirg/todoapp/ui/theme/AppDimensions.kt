package ru.gribbirg.todoapp.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * App distensions
 *
 * @see AppTheme
 */
data class AppDimensions(
    val paddingSmall: Dp = 4.dp,
    val paddingMedium: Dp = 8.dp,
    val paddingMediumLarge: Dp = 12.dp,
    val paddingLarge: Dp = 16.dp,
    val paddingExtraLarge: Dp = 32.dp,
    val paddingExtraExtraLarge: Dp = 48.dp,
    val paddingScreenMedium: Dp = 16.dp,

    val sizeItemMinHeight: Dp = 54.dp,

    val cardCornersRadius: Dp = 10.dp,

    val shadowElevationNo: Dp = 0.dp,
    val shadowElevationSmall: Dp = 2.dp,
    val shadowElevationLarge: Dp = 10.dp,

    val animationDuration: Int = 500,
    val animationDurationNavigationTransition: Int = 500,
)

internal val LocalDimensions = staticCompositionLocalOf { AppDimensions() }