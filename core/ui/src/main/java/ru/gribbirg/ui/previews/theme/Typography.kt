package ru.gribbirg.ui.previews.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import ru.gribbirg.theme.custom.AppTheme
import ru.gribbirg.theme.custom.AppTypography
import ru.gribbirg.theme.custom.TodoAppTheme
import ru.gribbirg.ui.previews.DefaultPreview
import java.lang.reflect.Modifier

@DefaultPreview
@Composable
private fun AppTypographyPreview() {
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