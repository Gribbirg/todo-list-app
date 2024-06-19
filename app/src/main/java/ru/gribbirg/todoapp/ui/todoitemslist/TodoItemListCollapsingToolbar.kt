package ru.gribbirg.todoapp.ui.todoitemslist

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import ru.gribbirg.todoapp.R

@Composable
fun TodoItemListCollapsingToolbar(
    topPadding: Dp,
    doneCount: Int?,
    filterState: TodoItemsListUiState.Loaded.FilterState?,
    onFilterChange: (TodoItemsListUiState.Loaded.FilterState) -> Unit,
    content: @Composable () -> Unit
) {
    val topBarState = rememberCollapsingToolbarScaffoldState()
    val systemUiController = rememberSystemUiController()


    CollapsingToolbarScaffold(modifier = Modifier
        .fillMaxSize()
        .padding(
            top = topPadding,
        ),
        state = topBarState,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        toolbarModifier = Modifier.setShadow(topBarState.toolbarState.progress),
        toolbar = {
            val progress = topBarState.toolbarState.progress

            val textSize = getToolbarValue(30, 18, progress).sp
            val leftPadding = getToolbarValue(60, 40, progress).dp
            val bottomPadding = getToolbarValue(30, 10, progress).dp

            val countColor = MaterialTheme.colorScheme.onSurfaceVariant
                .let { it.copy(alpha = getToolbarValue(it.alpha, 0f, progress)) }

            val boxColor =
                getToolbarValue(
                    MaterialTheme.colorScheme.background,
                    if (isSystemInDarkTheme()) {
                        MaterialTheme.colorScheme.surface
                    } else {
                        MaterialTheme.colorScheme.background
                    },
                    progress
                )

            systemUiController.setStatusBarColor(color = boxColor)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(boxColor)
                    .padding(0.dp)
                    .pin()
            )

            Text(
                text = stringResource(id = R.string.todo_items_list),
                modifier = Modifier
                    .road(
                        whenExpanded = Alignment.CenterStart,
                        whenCollapsed = Alignment.CenterStart
                    )
                    .padding(
                        start = leftPadding,
                        top = 16.dp,
                        end = 16.dp,
                        bottom = 10.dp
                    ),
                fontSize = textSize
            )

            if (doneCount != null)
                Text(
                    text = stringResource(
                        id = R.string.done_count,
                        doneCount
                    ),
                    modifier = Modifier
                        .road(
                            whenExpanded = Alignment.BottomStart,
                            whenCollapsed = Alignment.CenterStart
                        )
                        .padding(
                            start = leftPadding,
                            top = 16.dp,
                            end = 16.dp,
                            bottom = bottomPadding
                        ),
                    color = countColor,
                )

            IconButton(
                onClick = {
                    onFilterChange(
                        if (filterState == TodoItemsListUiState.Loaded.FilterState.ALL) {
                            TodoItemsListUiState.Loaded.FilterState.NOT_COMPLETED
                        } else {
                            TodoItemsListUiState.Loaded.FilterState.ALL
                        }
                    )
                },
                modifier = Modifier
                    .road(
                        whenExpanded = Alignment.BottomEnd,
                        whenCollapsed = Alignment.CenterEnd
                    )
                    .padding(
                        start = leftPadding,
                        top = 16.dp,
                        end = 16.dp,
                        bottom = 10.dp
                    )
                    .height(56.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                enabled = filterState != null
            ) {
                if (filterState != null && filterState == TodoItemsListUiState.Loaded.FilterState.ALL) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_visibility_off_24),
                        contentDescription = null
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_visibility_24),
                        contentDescription = null
                    )
                }
            }
        }
    ) {
        content()
    }
}

private fun Modifier.setShadow(progress: Float) =
    drawBehind {
        val size = size

        val shadowStart = Color.Black.copy(
            alpha = getToolbarValue(
                0.0f,
                0.32f,
                progress
            )
        )
        val shadowEnd = Color.Transparent

        if (progress < 1f) {
            drawRect(
                brush = Brush.verticalGradient(
                    listOf(shadowStart, shadowEnd),
                    startY = size.height,
                    endY = size.height + 28f
                ),
                topLeft = Offset(0f, size.height),
                size = Size(size.width, 28f),
            )
        }
    }

private fun getToolbarValue(startValue: Float, endValue: Float, progress: Float) =
    endValue + (startValue - endValue) * progress

private fun getToolbarValue(startValue: Int, endValue: Int, progress: Float) =
    getToolbarValue(startValue.toFloat(), endValue.toFloat(), progress)

private fun getToolbarValue(startValue: Color, endValue: Color, progress: Float) =
    Color(
        red = getToolbarValue(startValue.red, endValue.red, progress),
        blue = getToolbarValue(startValue.blue, endValue.blue, progress),
        green = getToolbarValue(startValue.green, endValue.green, progress),
    )