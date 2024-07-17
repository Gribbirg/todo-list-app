package ru.gribbirg.ui.previews.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import ru.gribbirg.theme.custom.AppColors
import ru.gribbirg.theme.custom.AppTheme
import ru.gribbirg.theme.custom.TodoAppTheme
import ru.gribbirg.ui.previews.DefaultPreview
import ru.gribbirg.ui.previews.ThemePreviews
import java.lang.reflect.Modifier

@OptIn(ExperimentalStdlibApi::class)
@DefaultPreview
@ThemePreviews
@Composable
private fun AppColorsPreview() {
    TodoAppTheme {
        Column(
            modifier = androidx.compose.ui.Modifier.width(250.dp).background(AppTheme.colors.primaryBack),
        ) {
            AppColors::class.java.declaredMethods.filter {
                Modifier.isPublic(it.modifiers) &&
                        !Modifier.isStatic(it.modifiers) &&
                        it.returnType == Long::class.java
            }.forEach { method ->
                val color = Color((method.invoke(AppTheme.colors) as Long).toColorInt())
                Row(
                    modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${method.name.replace("get", "").split("-")[0]}\n" +
                                "#${color.value.toHexString(HexFormat.UpperCase).substring(0, 8)}",
                        modifier = androidx.compose.ui.Modifier.weight(1f),
                        textAlign = TextAlign.End,
                        color = AppTheme.colors.primary
                    )
                    Spacer(modifier = androidx.compose.ui.Modifier.width(4.dp))
                    Box(
                        modifier = androidx.compose.ui.Modifier
                            .size(width = 100.dp, height = 50.dp)
                            .background(color)
                    )
                }
            }
        }
    }
}