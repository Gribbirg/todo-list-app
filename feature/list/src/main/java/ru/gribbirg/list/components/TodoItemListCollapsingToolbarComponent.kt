package ru.gribbirg.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import ru.gribbirg.list.TodoItemsListUiState
import ru.gribbirg.theme.custom.AppTheme
import ru.gribbirg.todoapp.list.R
import ru.gribbirg.ui.previews.DefaultPreview
import ru.gribbirg.ui.previews.LanguagePreviews
import ru.gribbirg.ui.previews.LayoutDirectionPreviews
import ru.gribbirg.ui.previews.OrientationPreviews
import ru.gribbirg.ui.previews.ScreenPreviewTemplate
import ru.gribbirg.ui.previews.TextPreviewParameterProvider
import ru.gribbirg.ui.previews.ThemePreviews

/**
 * Collapsing toolbar
 */
@Composable
internal fun TodoItemListCollapsingToolbar(
    topPadding: Dp,
    doneCount: Int?,
    filterState: TodoItemsListUiState.ListState.FilterState?,
    onFilterChange: (TodoItemsListUiState.ListState.FilterState) -> Unit,
    toSettingsScreen: () -> Unit,
    toAboutScreen: () -> Unit,
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
            val progress = topBarState.toolbarState
                .progress
                .let { if (it != 0f && it < 0.0001f) 1f else it }

            val textSize = getToolbarValue(
                AppTheme.typography.titleLarge.fontSize.value,
                AppTheme.typography.title.fontSize.value,
                progress
            ).sp
            val leftPadding = getToolbarValue(60f, 16f, progress).dp

            val countColor = AppTheme.colors.tertiary
                .let {
                    it.copy(
                        alpha = getToolbarValue(
                            it.alpha,
                            0f,
                            maxOf(0f, progress * 2 - 1f)
                        )
                    )
                }

            val boxColor =
                getToolbarValue(
                    AppTheme.colors.primaryBack,
                    AppTheme.colors.appBar,
                    progress
                )

            systemUiController.setStatusBarColor(color = boxColor)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(boxColor)
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
                        bottom = 20.dp
                    ),
                fontSize = textSize,
                color = AppTheme.colors.primary,
                style = AppTheme.typography.titleLarge
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
                            bottom = 20.dp
                        ),
                    color = countColor,
                    style = AppTheme.typography.body
                )

            Row(
                modifier = Modifier
                    .road(
                        whenExpanded = Alignment.BottomEnd,
                        whenCollapsed = Alignment.CenterEnd
                    )
                    .padding(
                        start = leftPadding,
                        top = 16.dp,
                        end = 16.dp,
                        bottom = 20.dp
                    ),
            ) {
                IconButton(
                    onClick = {
                        onFilterChange(
                            if (filterState == TodoItemsListUiState.ListState.FilterState.ALL) {
                                TodoItemsListUiState.ListState.FilterState.NOT_COMPLETED
                            } else {
                                TodoItemsListUiState.ListState.FilterState.ALL
                            }
                        )
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = AppTheme.colors.blue
                    ),
                    enabled = filterState != null
                ) {
                    FilterIcon(filterState = filterState)
                }
                MenuIconButton(
                    toAbout = toAboutScreen,
                    toSettings = toSettingsScreen
                )
            }
        }
    ) {
        content()
    }
}

@Composable
private fun FilterIcon(filterState: TodoItemsListUiState.ListState.FilterState?) {
    if (filterState != null && filterState == TodoItemsListUiState.ListState.FilterState.ALL) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_visibility_24),
            contentDescription = stringResource(id = R.string.show)
        )
    } else {
        Icon(
            painter = painterResource(id = R.drawable.baseline_visibility_off_24),
            contentDescription = stringResource(id = R.string.hide)
        )
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

private fun getToolbarValue(startValue: Color, endValue: Color, progress: Float) =
    Color(
        red = getToolbarValue(startValue.red, endValue.red, progress),
        blue = getToolbarValue(startValue.blue, endValue.blue, progress),
        green = getToolbarValue(startValue.green, endValue.green, progress),
    )

@DefaultPreview
@ThemePreviews
@LayoutDirectionPreviews
@LanguagePreviews
@OrientationPreviews
@Composable
private fun TodoItemListCollapsingToolbarPreview() {
    ScreenPreviewTemplate {
        var filterState by remember {
            mutableStateOf(TodoItemsListUiState.ListState.FilterState.ALL)
        }
        TodoItemListCollapsingToolbar(
            topPadding = it.calculateTopPadding(),
            doneCount = 8,
            filterState = filterState,
            onFilterChange = {
                filterState =
                    if (filterState == TodoItemsListUiState.ListState.FilterState.ALL)
                        TodoItemsListUiState.ListState.FilterState.NOT_COMPLETED
                    else
                        TodoItemsListUiState.ListState.FilterState.ALL
            },
            toSettingsScreen = {},
            toAboutScreen = {},
        ) {
            Text(
                text = TextPreviewParameterProvider().values.last(),
                modifier = Modifier.verticalScroll(
                    rememberScrollState()
                ),
                color = AppTheme.colors.secondary
            )
        }
    }
}