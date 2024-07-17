package ru.gribbirg.theme.material

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import ru.gribbirg.theme.custom.robotoFontFamily

internal val typography = Typography(
    displayLarge = TextStyle(
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 26.sp,
    ),
    displayMedium = TextStyle(
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 20.sp,
    ),
    displaySmall = TextStyle(
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 22.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 18.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 32.sp,
        lineHeight = 38.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.5.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.5.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.16.sp,
        textAlign = TextAlign.Center,
    ),
    labelMedium = TextStyle(
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.16.sp,
        textAlign = TextAlign.Center,
    ),
    labelSmall = TextStyle(
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.16.sp,
        textAlign = TextAlign.Center,
    ),
    bodyLarge = TextStyle(
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 26.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 20.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
)