package ru.gribbirg.todoapp.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

class AppColors(
    primary: Color,
    secondary: Color,
    tertiary: Color,
    disable: Color,
    separator: Color,
    overlay: Color,
    primaryBack: Color,
    secondaryBack: Color,
    elevated: Color,
    red: Color,
    green: Color,
    blue: Color,
    gray: Color,
    grayLight: Color,
    white: Color,
    appBar: Color,
    isDark: Boolean,
) {
    var primary by mutableStateOf(primary)
        private set
    var secondary by mutableStateOf(secondary)
        private set
    var tertiary by mutableStateOf(tertiary)
        private set
    var disable by mutableStateOf(disable)
        private set
    var separator by mutableStateOf(separator)
        private set
    var overlay by mutableStateOf(overlay)
        private set
    var primaryBack by mutableStateOf(primaryBack)
        private set
    var secondaryBack by mutableStateOf(secondaryBack)
        private set
    var elevated by mutableStateOf(elevated)
        private set
    var red by mutableStateOf(red)
        private set
    var green by mutableStateOf(green)
        private set
    var blue by mutableStateOf(blue)
        private set
    var gray by mutableStateOf(gray)
        private set
    var grayLight by mutableStateOf(grayLight)
        private set
    var white by mutableStateOf(white)
        private set
    var appBar by mutableStateOf(appBar)
        internal set
    var isDark by mutableStateOf(isDark)
        internal set

    fun copy(
        primary: Color = this.primary,
        secondary: Color = this.secondary,
        tertiary: Color = this.tertiary,
        disable: Color = this.disable,
        separator: Color = this.separator,
        overlay: Color = this.overlay,
        primaryBack: Color = this.primaryBack,
        secondaryBack: Color = this.secondaryBack,
        elevated: Color = this.elevated,
        red: Color = this.red,
        green: Color = this.green,
        blue: Color = this.blue,
        gray: Color = this.gray,
        grayLight: Color = this.grayLight,
        white: Color = this.white,
        appBar: Color = this.appBar,
        isDark: Boolean = this.isDark,
    ) = AppColors(
        primary,
        secondary,
        tertiary,
        disable,
        separator,
        overlay,
        primaryBack,
        secondaryBack,
        elevated,
        red,
        green,
        blue,
        gray,
        grayLight,
        white,
        appBar,
        isDark
    )

    fun updateColorsFrom(other: AppColors) {
        primary = other.primary
        secondary = other.secondary
        tertiary = other.tertiary
        disable = other.disable
        separator = other.separator
        overlay = other.overlay
        primaryBack = other.primaryBack
        secondaryBack = other.secondaryBack
        elevated = other.elevated
        red = other.red
        green = other.green
        blue = other.blue
        gray = other.gray
        grayLight = other.grayLight
        white = other.white
        appBar = other.appBar
    }

    companion object {
        private val White = Color(0xFFFFFFFF)
        private val Black = Color(0x00000000)

        // Light theme
        private val LightSeparator = Color(0x33000000)
        private val LightOverlay = Color(0x33000000)

        private val LightPrimaryLabel = Color(0xFF000000)
        private val LightSecondaryLabel = Color(0x99000000)
        private val LightTertiaryLabel = Color(0x4D000000)
        private val LightDisableLabel = Color(0x26000000)

        private val LightRed = Color(0xFFFF3B30)
        private val LightGreen = Color(0xFF34C759)
        private val LightBlue = Color(0xFF007AFF)
        private val LightGray = Color(0xFFD1D1D6)
        private val LightGrayLight = Color(0xFFD1D1D6)

        private val LightPrimaryBack = Color(0xFFF7F6F2)
        private val LightSecondaryBack = Color(0xFFFFFFFF)
        private val LightElevated = Color(0xFFFFFFFF)

        // Dark theme
        private val DarkSeparator = Color(0x33FFFFFF)
        private val DarkOverlay = Color(0x52000000)

        private val DarkPrimaryLabel = Color(0xFFFFFFFF)
        private val DarkSecondaryLabel = Color(0x99FFFFFF)
        private val DarkTertiaryLabel = Color(0x66FFFFFF)
        private val DarkDisableLabel = Color(0x26FFFFFF)

        private val DarkRed = Color(0xFFFF453A)
        private val DarkGreen = Color(0xFF32D74B)
        private val DarkBlue = Color(0xFF0A84FF)
        private val DarkGray = Color(0xFF8E8E93)
        private val DarkGrayLight = Color(0xFF48484A)

        private val DarkPrimaryBack = Color(0xFF161618)
        private val DarkSecondaryBack = Color(0xFF252528)
        private val DarkElevated = Color(0xFF3C3C3F)

        fun lightColors(
            primary: Color = LightPrimaryLabel,
            secondary: Color = LightSecondaryLabel,
            tertiary: Color = LightTertiaryLabel,
            disable: Color = LightDisableLabel,
            separator: Color = LightSeparator,
            overlay: Color = LightOverlay,
            primaryBack: Color = LightPrimaryBack,
            secondaryBack: Color = LightSecondaryBack,
            elevated: Color = LightElevated,
            red: Color = LightRed,
            green: Color = LightGreen,
            blue: Color = LightBlue,
            gray: Color = LightGray,
            grayLight: Color = LightGrayLight,
            appBar: Color = primaryBack,
            white: Color = White
        ) = AppColors(
            primary,
            secondary,
            tertiary,
            disable,
            separator,
            overlay,
            primaryBack,
            secondaryBack,
            elevated,
            red,
            green,
            blue,
            gray,
            grayLight,
            white,
            appBar,
            isDark = false
        )

        fun darkColors(
            primary: Color = DarkPrimaryLabel,
            secondary: Color = DarkSecondaryLabel,
            tertiary: Color = DarkTertiaryLabel,
            disable: Color = DarkDisableLabel,
            separator: Color = DarkSeparator,
            overlay: Color = DarkOverlay,
            primaryBack: Color = DarkPrimaryBack,
            secondaryBack: Color = DarkSecondaryBack,
            elevated: Color = DarkElevated,
            red: Color = DarkRed,
            green: Color = DarkGreen,
            blue: Color = DarkBlue,
            gray: Color = DarkGray,
            grayLight: Color = DarkGrayLight,
            appBar: Color = secondaryBack,
            white: Color = White
        ) = AppColors(
            primary,
            secondary,
            tertiary,
            disable,
            separator,
            overlay,
            primaryBack,
            secondaryBack,
            elevated,
            red,
            green,
            blue,
            gray,
            grayLight,
            white,
            appBar,
            isDark = true
        )
    }
}

val LocalColors = staticCompositionLocalOf { AppColors.lightColors() }