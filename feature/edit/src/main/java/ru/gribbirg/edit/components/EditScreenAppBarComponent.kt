package ru.gribbirg.edit.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import ru.gribbirg.edit.testing.EditFeatureTestingTags
import ru.gribbirg.theme.custom.AppTheme
import ru.gribbirg.todoapp.edit.R
import ru.gribbirg.ui.components.AnimatedTopAppBar
import ru.gribbirg.ui.components.CloseButton
import ru.gribbirg.ui.previews.BooleanPreviewParameterProvider
import ru.gribbirg.ui.previews.DefaultPreview
import ru.gribbirg.ui.previews.LanguagePreviews
import ru.gribbirg.ui.previews.OrientationPreviews
import ru.gribbirg.ui.previews.ScreenPreviewTemplate
import ru.gribbirg.ui.previews.TextPreviewParameterProvider
import ru.gribbirg.ui.previews.ThemePreviews

@Composable
internal fun EditScreenAppBarComponent(
    focusManager: FocusManager,
    onSave: () -> Unit,
    onClose: () -> Unit,
    saveEnabled: Boolean,
    scrollState: ScrollState,
) {
    AnimatedTopAppBar(
        scrollState = scrollState,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = {
                        focusManager.clearFocus()
                        onSave()
                        onClose()
                    },
                    modifier = Modifier.testTag(EditFeatureTestingTags.SAVE_BUTTON),
                    enabled = saveEnabled,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = AppTheme.colors.blue,
                        disabledContentColor = AppTheme.colors.disable
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.save),
                        textAlign = TextAlign.Center,
                        style = AppTheme.typography.button
                    )
                }
                Spacer(modifier = Modifier.width(AppTheme.dimensions.paddingSmall))
            }
        },
        navigationIcon = {
            CloseButton(onClick = onClose)
        }
    )
}

@DefaultPreview
@ThemePreviews
@LanguagePreviews
@OrientationPreviews
@Composable
private fun EditScreenAppBarComponentPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) enabled: Boolean,
) {
    ScreenPreviewTemplate {
        val scrollState = rememberScrollState()
        Scaffold(
            topBar = {
                EditScreenAppBarComponent(
                    focusManager = LocalFocusManager.current,
                    onSave = { },
                    onClose = { },
                    saveEnabled = enabled,
                    scrollState = scrollState,
                )
            },
            containerColor = AppTheme.colors.primaryBack
        ) { paddingValues ->
            Text(
                text = TextPreviewParameterProvider().values.last(),
                modifier = Modifier
                    .padding(paddingValues)
                    .verticalScroll(scrollState),
                color = AppTheme.colors.primary
            )
        }
    }
}