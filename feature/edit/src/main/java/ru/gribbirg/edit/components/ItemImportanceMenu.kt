package ru.gribbirg.edit.components

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.launch
import ru.gribbirg.domain.model.todo.TodoImportance
import ru.gribbirg.theme.AppTheme
import ru.gribbirg.todoapp.edit.R

// TODO: clean up
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ItemImportanceMenu(
    onChanged: (TodoImportance) -> Unit,
    onClose: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = sheetState,
        containerColor = AppTheme.colors.primaryBack,
        contentColor = AppTheme.colors.primary,
        scrimColor = AppTheme.colors.black.copy(alpha = 0.4f), // TODO: distensions
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.importance),
                style = AppTheme.typography.title,
                modifier = Modifier.padding(
                    horizontal = AppTheme.dimensions.paddingExtraLarge,
                    vertical = AppTheme.dimensions.paddingMedium
                ),
                color = AppTheme.colors.primary,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(intrinsicSize = IntrinsicSize.Max),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                for (importanceValue in TodoImportance.entries) {
                    val stockColor = AppTheme.colors.primary
                    val animColor = importanceValue.colorId?.let { colorResource(id = it) }
                        ?: AppTheme.colors.primary

                    val color = remember {
                        Animatable(stockColor)
                    }

                    Column(
                        modifier = Modifier
                            .padding(AppTheme.dimensions.paddingSmall)
                            .weight(1f)
                            .clip(RoundedCornerShape(AppTheme.dimensions.cardCornersRadius))
                            .clickable {
                                coroutineScope.launch {
                                    color.animateTo(
                                        targetValue = animColor,
                                        animationSpec = tween(
                                            durationMillis = 200,
                                            easing = EaseInOut
                                        )
                                    )
                                    color.animateTo(
                                        targetValue = stockColor,
                                        animationSpec = tween(
                                            durationMillis = 200,
                                            easing = EaseInOut
                                        )
                                    )
                                    onChanged(importanceValue)
                                    sheetState.hide()
                                    onClose()
                                }
                            }
                            .padding(AppTheme.dimensions.paddingLarge),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        importanceValue.logoId?.let {
                            Icon(
                                painterResource(id = it),
                                contentDescription = stringResource(id = importanceValue.nameId),
                                modifier = Modifier
                                    .shadow(
                                        AppTheme.dimensions.shadowElevationSmall,
                                        RoundedCornerShape(AppTheme.dimensions.cardCornersRadius)
                                    )
                                    .background(
                                        AppTheme.colors.elevated,
                                        RoundedCornerShape(AppTheme.dimensions.cardCornersRadius)
                                    )
                                    .padding(AppTheme.dimensions.paddingMedium),
                                tint = color.value,
                            )
                        } ?: Spacer(modifier = Modifier)
                        Spacer(modifier = Modifier.height(AppTheme.dimensions.paddingMedium))
                        Text(
                            text = stringResource(id = importanceValue.nameId),
                            style = AppTheme.typography.body
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
            Spacer(modifier = Modifier.height(AppTheme.dimensions.paddingLarge))
        }
    }
}