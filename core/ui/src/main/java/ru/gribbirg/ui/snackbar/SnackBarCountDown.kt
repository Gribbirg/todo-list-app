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
                color = backColor.copy(alpha = 0.12f), // TODO: magic num
                style = strokeStyle
            )
            drawArc(
                color = color,
                startAngle = -90f, // TODO: magic num
                sweepAngle = (-360f * timerProgress), // TODO: magic num
                useCenter = false,
                style = strokeStyle
            )
        }
        Text(
            text = secondsRemaining.toString(),
            style = AppTheme.typography.body,
            color = color,
        )
    }
}