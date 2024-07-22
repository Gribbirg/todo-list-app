package ru.gribbirg.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.gribbirg.domain.model.user.ThemeSettings
import ru.gribbirg.theme.custom.AppTheme
import ru.gribbirg.ui.previews.DefaultPreview
import ru.gribbirg.ui.previews.ItemPreviewTemplate
import ru.gribbirg.ui.previews.LanguagePreviews
import ru.gribbirg.ui.previews.LayoutDirectionPreviews
import ru.gribbirg.ui.previews.ThemePreviews

@Composable
internal fun ThemeSelectorRow(
    selected: ThemeSettings,
    onSelect: (ThemeSettings) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        ThemeSettings.entries.forEach { value ->
            ThemeSelectorRowRadioRow(
                themeValue = value,
                selected = selected == value,
                onSelect = { onSelect(value) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun ThemeSelectorRowRadioRow(
    themeValue: ThemeSettings,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier
            .clickable(onClick = onSelect)
            .semantics(mergeDescendants = true) {
                this.selected = selected
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect,
            modifier = Modifier.clearAndSetSemantics { },
            colors = RadioButtonDefaults.colors(
                selectedColor = AppTheme.colors.blue,
                unselectedColor = AppTheme.colors.gray,
                disabledSelectedColor = AppTheme.colors.disable,
                disabledUnselectedColor = AppTheme.colors.disable,
            ),
            enabled = enabled,
        )
        Text(
            text = stringResource(id = themeValue.textId),
            style = AppTheme.typography.body,
            color = AppTheme.colors.primary,
        )
    }
}

@DefaultPreview
@ThemePreviews
@LayoutDirectionPreviews
@LanguagePreviews
@Composable
private fun ThemeSelectorRowPreview(
    @PreviewParameter(provider = ThemeSettingsPreviewParameterProvider::class)
    startTheme: ThemeSettings,
) {
    var selectedTheme by remember {
        mutableStateOf(startTheme)
    }
    ItemPreviewTemplate {
        ThemeSelectorRow(
            selected = selectedTheme,
            onSelect = { selectedTheme = it },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

internal class ThemeSettingsPreviewParameterProvider :
    PreviewParameterProvider<ThemeSettings> {
    override val values: Sequence<ThemeSettings>
        get() = sequenceOf(
            *ThemeSettings.entries.toTypedArray(),
        )
}