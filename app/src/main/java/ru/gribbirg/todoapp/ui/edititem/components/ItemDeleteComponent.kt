package ru.gribbirg.todoapp.ui.edititem.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.gribbirg.todoapp.R
import ru.gribbirg.todoapp.ui.theme.AppTheme

@Composable
internal fun ItemDelete(
    enabled: Boolean,
    onDeleted: () -> Unit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    TextButton(
        onClick = {
            onClick()
            onDeleted()
        },
        modifier = modifier,
        colors = ButtonDefaults.textButtonColors(
            contentColor = AppTheme.colors.red,
            disabledContentColor = AppTheme.colors.disable
        ),
        enabled = enabled
    ) {
        Icon(Icons.Filled.Delete, contentDescription = stringResource(id = R.string.delelte))
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = stringResource(id = R.string.delelte),
            style = AppTheme.typography.body
        )
    }
}