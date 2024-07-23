package ru.gribbirg.ui.snackbar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import ru.gribbirg.theme.custom.AppTheme
import ru.gribbirg.ui.extensions.scaledSp
import ru.gribbirg.ui.snackbar.SnackBarConstants.CIRCLE_BACK_ANGLE
import ru.gribbirg.ui.snackbar.SnackBarConstants.COUNT_DOWN_CIRCLE_BACKGROUND_ALPHA
import ru.gribbirg.ui.snackbar.SnackBarConstants.COUNT_DOWN_CIRCLE_START_ANGLE

@Composable
internal fun SnackBarCountDown(
    timerProgress: Float,
    secondsRemaining: Int,
    color: Color,
    backColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val width = AppTheme.dimensions.lineWidthMedium
        Canvas(Modifier.matchParentSize()) {
            val strokeStyle = Stroke(
                width = width.toPx(),
                cap = StrokeCap.Round
            )
            drawCircle(
                color = backColor.copy(alpha = COUNT_DOWN_CIRCLE_BACKGROUND_ALPHA),
                style = strokeStyle
            )
            drawArc(
                color = color,
                startAngle = COUNT_DOWN_CIRCLE_START_ANGLE,
                sweepAngle = (CIRCLE_BACK_ANGLE * timerProgress),
                useCenter = false,
                style = strokeStyle
            )
        }
        Text(
            text = secondsRemaining.toString(),
            style = AppTheme.typography.body,
            color = color,
            fontSize = AppTheme.typography.body.fontSize.value.toInt().scaledSp
        )
    }
}