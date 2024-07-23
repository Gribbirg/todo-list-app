package ru.gribbirg.ui.snackbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.gribbirg.theme.custom.AppTheme
import ru.gribbirg.ui.snackbar.SnackBarConstants.ANIM_DELAY
import ru.gribbirg.ui.snackbar.SnackBarConstants.DEFAULT_DURATION_IN_SECONDS
import ru.gribbirg.ui.snackbar.SnackBarConstants.MILLIS_IN_SECOND
import ru.gribbirg.ui.snackbar.SnackBarConstants.TARGET_OFFSET_MULTIPLE

@Composable
fun CountDownSnackBar(
    snackBarData: SnackbarData,
    modifier: Modifier = Modifier,
    durationInSeconds: Int = DEFAULT_DURATION_IN_SECONDS,
) {
    val scope = rememberCoroutineScope()
    val inOutAnimDuration = AppTheme.dimensions.animationDurationShort

    var shown by remember { mutableStateOf(false) }
    val totalDuration = remember(durationInSeconds) { durationInSeconds * MILLIS_IN_SECOND }
    var millisRemaining by remember { mutableIntStateOf(totalDuration) }

    LaunchedEffect(snackBarData) {
        shown = true
        while (millisRemaining > 0) {
            delay(ANIM_DELAY.toLong())
            millisRemaining -= ANIM_DELAY
        }
        shown = false
        delay(inOutAnimDuration.toLong())
        snackBarData.dismiss()
    }

    val actionLabel = snackBarData.visuals.actionLabel
    val actionComposable: (@Composable () -> Unit)? = if (actionLabel != null) {
        @Composable {
            TextButton(
                colors = ButtonDefaults.textButtonColors(contentColor = AppTheme.colors.blue),
                onClick = {
                    scope.launch {
                        shown = false
                        millisRemaining = maxOf(millisRemaining, inOutAnimDuration * 2)
                        delay(inOutAnimDuration.toLong())
                        snackBarData.performAction()
                    }
                },
                content = { Text(actionLabel, style = AppTheme.typography.button) }
            )
        }
    } else {
        null
    }

    val dismissActionComposable: (@Composable () -> Unit)? =
        if (snackBarData.visuals.withDismissAction) {
            @Composable {
                IconButton(
                    onClick = { snackBarData.dismiss() },
                    content = {
                        Icon(Icons.Rounded.Close, null)
                    }
                )
            }
        } else {
            null
        }

    AnimatedVisibility(
        visible = shown,
        enter = slideInVertically(
            initialOffsetY = { it * TARGET_OFFSET_MULTIPLE },
            animationSpec = tween(durationMillis = inOutAnimDuration)
        ),
        exit = slideOutVertically(
            targetOffsetY = { it * TARGET_OFFSET_MULTIPLE },
            animationSpec = tween(durationMillis = inOutAnimDuration)
        ),
    ) {
        Snackbar(
            modifier = modifier
                .padding(AppTheme.dimensions.paddingMediumLarge)
                .semantics { role = Role.Tab },
            action = actionComposable,
            actionOnNewLine = false,
            dismissAction = dismissActionComposable,
            dismissActionContentColor = AppTheme.colors.disable,
            actionContentColor = AppTheme.colors.blue,
            containerColor = AppTheme.colors.elevated,
            contentColor = AppTheme.colors.primary,
            shape = SnackbarDefaults.shape,
        ) {
            Row(
                modifier = Modifier.semantics(mergeDescendants = true) {
                    liveRegion = LiveRegionMode.Assertive
                },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingMediumLarge)
            ) {
                SnackBarCountDown(
                    timerProgress = millisRemaining.toFloat() / totalDuration.toFloat(),
                    secondsRemaining = (millisRemaining / MILLIS_IN_SECOND) + 1,
                    color = AppTheme.colors.primary,
                    backColor = AppTheme.colors.disable,
                    modifier = Modifier
                        .size(AppTheme.dimensions.sizeItemMedium)
                        .clearAndSetSemantics { }
                )
                Text(
                    snackBarData.visuals.message,
                    style = AppTheme.typography.body,
                    color = AppTheme.colors.primary,
                    maxLines = 1,
                )
            }
        }
    }
}