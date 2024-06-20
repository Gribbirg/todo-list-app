package ru.gribbirg.todoapp.ui.edititem.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.gribbirg.todoapp.R
import ru.gribbirg.todoapp.ui.theme.AppTheme

@Composable
internal fun ItemTextField(
    text: String,
    onChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val indicatorColor = Color.Transparent
    val containerColor = AppTheme.colors.secondaryBack
    val textColor = AppTheme.colors.primary
    val placeFolderColor = AppTheme.colors.gray
    val cursorColor = AppTheme.colors.blue
    TextField(
        value = text,
        onValueChange = { onChanged(it) },
        modifier = modifier,
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
        shape = RoundedCornerShape(10.dp),
        textStyle = AppTheme.typography.body
    )
}