package ru.gribbirg.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.gribbirg.todoapp.ui.R
import ru.gribbirg.ui.previews.DefaultPreview
import java.lang.reflect.Modifier

/**
 * App typography
 */
data class AppTypography(
    val titleLarge: TextStyle = TextStyle(
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 32.sp,
        lineHeight = 38.sp,
    ),
    val title: TextStyle = TextStyle(
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.5.sp,
    ),
    val button: TextStyle = TextStyle(
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.16.sp,
        textAlign = TextAlign.Center,
    ),
    val body: TextStyle = TextStyle(
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 20.sp,
    ),
    val subhead: TextStyle = TextStyle(
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    )
)

val robotoFontFamily = FontFamily(
    Font(R.font.roboto_black, FontWeight.Black),
    Font(R.font.roboto_blackitalic, FontWeight.Black, FontStyle.Italic),
    Font(R.font.roboto_bold, FontWeight.Bold),
    Font(R.font.roboto_bolditalic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.roboto_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.roboto_light, FontWeight.Light),
    Font(R.font.roboto_lightitalic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.roboto_medium, FontWeight.Medium),
    Font(R.font.roboto_mediumitalic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_thin, FontWeight.Thin),
    Font(R.font.roboto_thinitalic, FontWeight.Thin, FontStyle.Italic),
)

internal val LocalTypography = staticCompositionLocalOf { AppTypography() }

@DefaultPreview
@Composable
private fun AppColorsPreview() {
    TodoAppTheme {
        Column(
            modifier = androidx.compose.ui.Modifier.width(500.dp),
        ) {
            AppTypography::class.java.declaredMethods.filter {
                Modifier.isPublic(it.modifiers)
                        && !Modifier.isStatic(it.modifiers)
                        && it.returnType == TextStyle::class.java
                        && "get" in it.name
            }
                .map { it to it.invoke(AppTheme.typography) as TextStyle }
                .sortedByDescending { it.second.fontSize.value }
                .forEach { (method, style) ->
                    Text(
                        text = "${
                            method.name.replace(
                                "get",
                                ""
                            )
                        } — ${style.fontSize.value.toInt()} / ${style.lineHeight.value.toInt()}",
                        modifier = androidx.compose.ui.Modifier.padding(AppTheme.dimensions.paddingLarge),
                        style = style,
                        color = AppTheme.colors.primary
                    )
                }
        }
    }
}