package ru.gribbirg.todoapp.ui.todoitemslist

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import ru.gribbirg.todoapp.R
import ru.gribbirg.todoapp.data.data.TodoItem

@Composable
fun TodoListItemScreen(
    viewModel: TodoItemsListViewModel,
    toEditItemScreen: (item: TodoItem?) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val lazyListState = rememberLazyListState()
    val topBarState = rememberCollapsingToolbarScaffoldState()
    val systemUiController = rememberSystemUiController()


    Scaffold { paddingValue ->
        CollapsingToolbarScaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValue.calculateTopPadding(),
                ),
            state = topBarState,
            scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
            toolbarModifier = Modifier
                .drawBehind {
                    val size = size

                    val shadowStart = Color.Black.copy(
                        alpha = getToolbarValue(
                            0.0f,
                            0.32f,
                            topBarState.toolbarState.progress

                        )
                    )
                    val shadowEnd = Color.Transparent

                    if (topBarState.toolbarState.progress < 1f) {
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
                },
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

                if (uiState is TodoItemsListUiState.Loaded)
                    Text(
                        text = stringResource(
                            id = R.string.done_count,
                            (uiState as TodoItemsListUiState.Loaded).doneCount
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
                        viewModel.onFilterChange(
                            if ((uiState as TodoItemsListUiState.Loaded)
                                    .filterState == TodoItemsListUiState.Loaded.FilterState.ALL
                            ) {
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
                    enabled = uiState is TodoItemsListUiState.Loaded
                ) {
                    if (uiState is TodoItemsListUiState.Loaded &&
                        (uiState as TodoItemsListUiState.Loaded).filterState == TodoItemsListUiState.Loaded.FilterState.ALL
                    ) {
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
            when (uiState) {
                is TodoItemsListUiState.Loaded -> {
                    val state = uiState as TodoItemsListUiState.Loaded
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 8.dp,
                                end = 8.dp
                            ),
                        userScrollEnabled = true,
                        state = lazyListState
                    ) {
                        if (state.items.isNotEmpty()) {
                            item {
                                Spacer(modifier = Modifier.height(5.dp))
                            }
                            item {
                                TodoItemView(
                                    item = state.items.first(),
                                    index = 0,
                                    onChecked = viewModel::onChecked,
                                    onInfoClicked = toEditItemScreen,
                                    topBorderRadius = 16.dp
                                )
                            }
                            items(state.items.size - 1) {
                                val item = state.items[it + 1]
                                TodoItemView(
                                    item = item,
                                    index = it + 1,
                                    onChecked = viewModel::onChecked,
                                    onInfoClicked = toEditItemScreen
                                )
                            }
                            item {
                                Row(
                                    modifier = Modifier
                                        .clip(
                                            RoundedCornerShape(
                                                bottomEnd = 16.dp,
                                                bottomStart = 16.dp
                                            )
                                        )
                                        .background(MaterialTheme.colorScheme.surface)
                                        .fillMaxWidth()
                                        .clickable { toEditItemScreen(null) }
                                ) {
                                    Spacer(modifier = Modifier.width(45.dp))
                                    Text(
                                        text = stringResource(id = R.string.new_item),
                                        modifier = Modifier.padding(20.dp),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        } else {
                            item {
                                Row(
                                    modifier = Modifier
                                        .clip(
                                            RoundedCornerShape(
                                                bottomEnd = 16.dp,
                                                bottomStart = 16.dp,
                                                topEnd = 16.dp,
                                                topStart = 16.dp
                                            )
                                        )
                                        .background(MaterialTheme.colorScheme.surface)
                                        .fillMaxWidth()
                                        .clickable { toEditItemScreen(null) }
                                ) {
                                    Spacer(modifier = Modifier.width(45.dp))
                                    Text(
                                        text = stringResource(id = R.string.new_item),
                                        modifier = Modifier.padding(20.dp),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(40.dp))
                        }
                    }
                }

                else -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

@Composable
private fun TodoItemView(
    item: TodoItem,
    index: Int,
    onChecked: (TodoItem, Boolean) -> Unit,
    onInfoClicked: (TodoItem) -> Unit,
    topBorderRadius: Dp = 0.dp,
) {
    val shape = RoundedCornerShape(
        topStart = topBorderRadius,
        topEnd = topBorderRadius,
    )

    Row(
        modifier = Modifier
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
            .padding(
                start = 8.dp,
                end = 8.dp
            )
    ) {
        Checkbox(checked = item.completed, onCheckedChange = { onChecked(item, it) })
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text(
                text = item.text,
                modifier = Modifier,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                textDecoration =
                if (item.completed)
                    TextDecoration.LineThrough
                else
                    null,
                color = if (item.completed)
                    MaterialTheme.colorScheme.onSurfaceVariant
                else
                    MaterialTheme.colorScheme.onBackground
            )
            if (item.deadline != null) {
                Text(
                    text = stringResource(
                        id = R.string.day_month_date_template,
                        item.deadline.dayOfMonth,
                        stringArrayResource(id = R.array.months_names)[item.deadline.monthValue]
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        IconButton(
            onClick = { onInfoClicked(item) },
            colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.onSurface)
        ) {
            Icon(Icons.Outlined.Info, contentDescription = "")
        }
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