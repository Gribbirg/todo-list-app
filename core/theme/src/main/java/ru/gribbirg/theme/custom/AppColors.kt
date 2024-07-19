package ru.gribbirg.theme.custom

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import ru.gribbirg.theme.Black
import ru.gribbirg.theme.DarkBlue
import ru.gribbirg.theme.DarkDisableLabel
import ru.gribbirg.theme.DarkElevated
import ru.gribbirg.theme.DarkGray
import ru.gribbirg.theme.DarkGrayLight
import ru.gribbirg.theme.DarkGreen
import ru.gribbirg.theme.DarkOverlay
import ru.gribbirg.theme.DarkPrimaryBack
import ru.gribbirg.theme.DarkPrimaryLabel
import ru.gribbirg.theme.DarkRed
import ru.gribbirg.theme.DarkSecondaryBack
import ru.gribbirg.theme.DarkSecondaryLabel
import ru.gribbirg.theme.DarkSeparator
import ru.gribbirg.theme.DarkTertiaryLabel
import ru.gribbirg.theme.LightBlue
import ru.gribbirg.theme.LightDisableLabel
import ru.gribbirg.theme.LightElevated
import ru.gribbirg.theme.LightGray
import ru.gribbirg.theme.LightGrayLight
import ru.gribbirg.theme.LightGreen
import ru.gribbirg.theme.LightOverlay
import ru.gribbirg.theme.LightPrimaryBack
import ru.gribbirg.theme.LightPrimaryLabel
import ru.gribbirg.theme.LightRed
import ru.gribbirg.theme.LightSecondaryBack
import ru.gribbirg.theme.LightSecondaryLabel
import ru.gribbirg.theme.LightSeparator
import ru.gribbirg.theme.LightTertiaryLabel
import ru.gribbirg.theme.White

/**
 * App colors
 *
 * @see AppTheme
 */
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
    black: Color,
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
    var black by mutableStateOf(black)
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
        black: Color = this.black,
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
        black,
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
        black = other.black
        appBar = other.appBar
        isDark = other.isDark
    }

    companion object {

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
            black: Color = Black,
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
            black,
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
            black: Color = Black,
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
            black,
            appBar,
            isDark = true
        )
    }
}

val LocalColors = staticCompositionLocalOf { AppColors.lightColors() }
