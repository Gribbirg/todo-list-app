package ru.gribbirg.edit.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import ru.gribbirg.todoapp.edit.R
import ru.gribbirg.ui.previews.BooleanPreviewParameterProvider
import ru.gribbirg.ui.previews.DefaultPreview
import ru.gribbirg.ui.previews.LanguagePreviews
import ru.gribbirg.ui.previews.OrientationPreviews
import ru.gribbirg.ui.previews.ScreenPreviewTemplate
import ru.gribbirg.ui.previews.TextPreviewParameterProvider
import ru.gribbirg.ui.previews.ThemePreviews
import ru.gribbirg.ui.theme.AppTheme

@Composable
@OptIn(ExperimentalMaterial3Api::class)
internal fun EditScreenAppBarComponent(
    focusManager: FocusManager,
    onSave: () -> Unit,
    onClose: () -> Unit,
    saveEnabled: Boolean,
    scrollState: ScrollState,
) {
    val systemUiController = rememberSystemUiController()

    val appBarColor = AppTheme.colors.primaryBack
    val scrolledAppBarColor = AppTheme.colors.appBar

    val color = remember {
        androidx.compose.animation.Animatable(if (scrollState.canScrollBackward) appBarColor else scrolledAppBarColor)
    }

    val elevationNo = AppTheme.dimensions.shadowElevationNo.value
    val elevationFull = AppTheme.dimensions.shadowElevationLarge.value

    val elevation = remember {
        Animatable(if (scrollState.canScrollBackward) elevationFull else elevationNo)
    }

    LaunchedEffect(scrollState.canScrollBackward) {
        launch { elevation.animateTo(if (scrollState.canScrollBackward) elevationFull else elevationNo) }
        launch { color.animateTo(if (scrollState.canScrollBackward) scrolledAppBarColor else appBarColor) }
    }
    systemUiController.setStatusBarColor(color.value)

    TopAppBar(
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
        modifier = Modifier
            .shadow(
                elevation = elevation.value.dp
            )
            .background(color.value),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent
        ),
        navigationIcon = {
            IconButton(onClick = onClose) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = stringResource(id = R.string.close),
                    tint = AppTheme.colors.primary
                )
            }
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