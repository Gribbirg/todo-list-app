package ru.gribbirg.theme.material

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import ru.gribbirg.theme.Black
import ru.gribbirg.theme.DarkBlue
import ru.gribbirg.theme.DarkGray
import ru.gribbirg.theme.DarkGrayLight
import ru.gribbirg.theme.DarkGreen
import ru.gribbirg.theme.DarkOverlay
import ru.gribbirg.theme.DarkPrimaryBack
import ru.gribbirg.theme.DarkPrimaryLabel
import ru.gribbirg.theme.DarkRed
import ru.gribbirg.theme.DarkSecondaryBack
import ru.gribbirg.theme.DarkTertiaryLabel
import ru.gribbirg.theme.LightBlue
import ru.gribbirg.theme.LightGray
import ru.gribbirg.theme.LightGrayLight
import ru.gribbirg.theme.LightGreen
import ru.gribbirg.theme.LightOverlay
import ru.gribbirg.theme.LightPrimaryBack
import ru.gribbirg.theme.LightPrimaryLabel
import ru.gribbirg.theme.LightRed
import ru.gribbirg.theme.LightSecondaryBack
import ru.gribbirg.theme.LightTertiaryLabel
import ru.gribbirg.theme.White

internal val lightColorSchemeImpl = lightColorScheme(
    primary = LightBlue,
    onPrimary = White,
    primaryContainer = LightPrimaryBack,
    onPrimaryContainer = LightPrimaryLabel,
    inversePrimary = Black,
    secondary = LightGreen,
    onSecondary = White,
    secondaryContainer = LightSecondaryBack,
    onSecondaryContainer = LightSecondaryBack,
    tertiary = LightRed,
    onTertiary = White,
    tertiaryContainer = LightGray,
    onTertiaryContainer = LightTertiaryLabel,
    background = LightPrimaryBack,
    onBackground = LightPrimaryLabel,
    surface = LightGray,
    onSurface = LightGrayLight,
    surfaceVariant = LightGrayLight,
    onSurfaceVariant = White,
    surfaceTint = LightBlue,
    inverseSurface = DarkGrayLight,
    inverseOnSurface = DarkGrayLight,
    error = LightRed,
    onError = White,
    errorContainer = White,
    onErrorContainer = LightRed,
    outline = LightOverlay,
    outlineVariant = LightGrayLight,
    scrim = White,
    surfaceBright = LightGrayLight,
    surfaceContainer = LightGray,
    surfaceContainerHigh = LightGrayLight,
    surfaceContainerHighest = White,
    surfaceContainerLow = LightGray,
    surfaceContainerLowest = LightGray,
    surfaceDim = Black,
)

internal val darkColorSchemeImpl = darkColorScheme(
    primary = DarkBlue,
    onPrimary = Black,
    primaryContainer = DarkPrimaryBack,
    onPrimaryContainer = DarkPrimaryLabel,
    inversePrimary = White,
    secondary = DarkGreen,
    onSecondary = Black,
    secondaryContainer = DarkSecondaryBack,
    onSecondaryContainer = DarkSecondaryBack,
    tertiary = DarkRed,
    onTertiary = Black,
    tertiaryContainer = DarkGray,
    onTertiaryContainer = DarkTertiaryLabel,
    background = DarkPrimaryBack,
    onBackground = DarkPrimaryLabel,
    surface = DarkGray,
    onSurface = DarkGrayLight,
    surfaceVariant = DarkGrayLight,
    onSurfaceVariant = Black,
    surfaceTint = DarkBlue,
    inverseSurface = LightGrayLight,
    inverseOnSurface = LightGrayLight,
    error = DarkRed,
    onError = Black,
    errorContainer = Black,
    onErrorContainer = DarkRed,
    outline = DarkOverlay,
    outlineVariant = DarkGrayLight,
    scrim = Black,
    surfaceBright = DarkGrayLight,
    surfaceContainer = DarkGray,
    surfaceContainerHigh = DarkGrayLight,
    surfaceContainerHighest = Black,
    surfaceContainerLow = DarkGray,
    surfaceContainerLowest = DarkGray,
    surfaceDim = White,
)