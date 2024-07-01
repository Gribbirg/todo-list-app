package ru.gribbirg.todoapp.ui.edititem.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import ru.gribbirg.todoapp.R
import ru.gribbirg.todoapp.ui.previews.DefaultPreview
import ru.gribbirg.todoapp.ui.previews.ItemPreviewTemplate
import ru.gribbirg.todoapp.ui.previews.LanguagePreviews
import ru.gribbirg.todoapp.ui.previews.LayoutDirectionPreviews
import ru.gribbirg.todoapp.ui.previews.ThemePreviews
import ru.gribbirg.todoapp.ui.theme.AppTheme

@Composable
internal fun ItemTextField(
    text: String,
    onChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(AppTheme.dimensions.cardCornersRadius),
) {
    val indicatorColor = Color.Transparent
    val containerColor = AppTheme.colors.secondaryBack
    val textColor = AppTheme.colors.primary
    val placeFolderColor = AppTheme.colors.gray
    val cursorColor = AppTheme.colors.blue
    TextField(
        value = text,
        onValueChange = { onChanged(it) },
        modifier = modifier.shadow(AppTheme.dimensions.shadowElevationSmall, shape),
        minLines = 5,
        placeholder = { Text(text = stringResource(id = R.string.type_text)) },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = indicatorColor,
            unfocusedIndicatorColor = indicatorColor,
            disabledIndicatorColor = indicatorColor,
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            disabledContainerColor = containerColor,
            focusedTextColor = textColor,
            unfocusedTextColor = textColor,
            disabledTextColor = textColor,
            focusedPlaceholderColor = placeFolderColor,
            disabledPlaceholderColor = placeFolderColor,
            unfocusedPlaceholderColor = placeFolderColor,
            cursorColor = cursorColor
        ),
        shape = shape,
        textStyle = AppTheme.typography.body
    )
}

@DefaultPreview
@ThemePreviews
@LanguagePreviews
@LayoutDirectionPreviews
@Composable
private fun ItemTextFieldPreview() {
    ItemPreviewTemplate {
        var text by remember { mutableStateOf("") }
        ItemTextField(text = text, onChanged = { text = it })
    }
}