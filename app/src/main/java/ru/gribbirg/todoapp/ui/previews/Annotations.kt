package ru.gribbirg.todoapp.ui.previews

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

/**
 * Annotations for previews
 */
@Preview(
    name = "Default Mode",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO,
    locale = "en",
    fontScale = 1.0f
)
annotation class DefaultPreview

@Preview(name = "Dark Mode", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
annotation class ThemePreviews

@Preview(
    name = "Landscape Mode",
    showBackground = true,
    device = Devices.AUTOMOTIVE_1024p,
    widthDp = 640
)
annotation class OrientationPreviews

@Preview(name = "Large Font Size", fontScale = 1.5f)
@Preview(name = "Extra Large Font Size", fontScale = 2f)
annotation class FontScalePreviews

@Preview(name = "Right-To-Left", locale = "ar")
annotation class LayoutDirectionPreviews

@Preview(name = "Russian language", locale = "ru")
annotation class LanguagePreviews