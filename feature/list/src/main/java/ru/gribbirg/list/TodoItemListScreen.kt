package ru.gribbirg.list

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yandex.authsdk.YandexAuthResult
import ru.gribbirg.domain.model.TodoItem
import ru.gribbirg.list.components.BoxWithSidesForShadow
import ru.gribbirg.list.components.Sides
import ru.gribbirg.list.components.TodoItemListCollapsingToolbar
import ru.gribbirg.list.components.TodoItemRow
import ru.gribbirg.list.components.TodoItemsListPullToRefreshBox
import ru.gribbirg.todoapp.list.R
import ru.gribbirg.ui.components.ErrorComponent
import ru.gribbirg.ui.components.LoadingComponent
import ru.gribbirg.ui.previews.DefaultPreview
import ru.gribbirg.ui.previews.FontScalePreviews
import ru.gribbirg.ui.previews.LanguagePreviews
import ru.gribbirg.ui.previews.LayoutDirectionPreviews
import ru.gribbirg.ui.previews.OrientationPreviews
import ru.gribbirg.ui.previews.ScreenPreviewTemplate
import ru.gribbirg.ui.previews.ThemePreviews
import ru.gribbirg.ui.previews.TodoItemPreviewParameterProvider
import ru.gribbirg.ui.theme.AppTheme

/**
 * Main app screen with list of items
 *
 * @see TodoItemsListUiState
 * @see TodoItemsListViewModel
 */
@Composable
fun TodoListItemScreen(
    viewModel: TodoItemsListViewModel,
    toEditItemScreen: (itemId: String?) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    TodoItemsListScreenContent(
        uiState = uiState,
        toEditItemScreen = toEditItemScreen,
        onFilterChange = viewModel::onFilterChange,
        onChecked = viewModel::onChecked,
        onDelete = viewModel::delete,
        onRefresh = viewModel::onUpdate,
        onLogin = viewModel::onLogin,
        onExit = viewModel::onExit,
        onResetEvent = viewModel::onResetEvent,
    )
}

@Composable
private fun TodoItemsListScreenContent(
    uiState: TodoItemsListUiState,
    toEditItemScreen: (itemId: String?) -> Unit,
    onFilterChange: (TodoItemsListUiState.ListState.FilterState) -> Unit,
    onChecked: (TodoItem, Boolean) -> Unit,
    onDelete: (TodoItem) -> Unit,
    onRefresh: () -> Unit,
    onLogin: (YandexAuthResult) -> Unit,
    onExit: () -> Unit,
    onResetEvent: () -> Unit,
) {
    val lazyListState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(uiState.eventState?.time) {
        when (uiState.eventState) {
            is TodoItemsListUiState.EventState.ShowSnackBar -> {
                snackbarHostState.showSnackbar(context.getString(uiState.eventState.textId))
            }

            null -> {}
        }
        onResetEvent()
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        containerColor = AppTheme.colors.primaryBack,
        floatingActionButton = {
            if (uiState.listState is TodoItemsListUiState.ListState.Loaded)
                FloatingActionButton(
                    onClick = { toEditItemScreen(null) },
                    shape = CircleShape,
                    containerColor = AppTheme.colors.blue,
                    contentColor = AppTheme.colors.white
                ) {
                    Icon(Icons.Filled.Add, contentDescription = stringResource(id = R.string.add))
                }
        }
    ) { paddingValue ->
        TodoItemsListPullToRefreshBox(
            isRefreshing = uiState.networkState is TodoItemsListUiState.NetworkState.Updating,
            onRefresh = onRefresh,
            enabled = uiState.loginState is TodoItemsListUiState.LoginState.Auth,
            modifier = Modifier.fillMaxSize()
        ) {
            TodoItemListCollapsingToolbar(
                topPadding = paddingValue.calculateTopPadding(),
                doneCount = (uiState.listState as? TodoItemsListUiState.ListState.Loaded)?.doneCount,
                filterState = (uiState.listState as? TodoItemsListUiState.ListState.Loaded)?.filterState,
                isInAccount = uiState.loginState is TodoItemsListUiState.LoginState.Auth,
                onFilterChange = onFilterChange,
                onLogin = onLogin,
                onExit = onExit,
            ) {
                when (uiState.listState) {
                    is TodoItemsListUiState.ListState.Loaded -> {
                        ListLoadedContent(
                            lazyListState = lazyListState,
                            listState = uiState.listState,
                            onChecked = onChecked,
                            onDelete = onDelete,
                            toEditItemScreen = toEditItemScreen,
                            modifier = Modifier
                                .animateContentSize()
                                .fillMaxWidth()
                                .padding(
                                    start = AppTheme.dimensions.paddingMedium,
                                    end = AppTheme.dimensions.paddingMedium,
                                ),
                            bottomPadding = paddingValue.calculateBottomPadding(),
                        )
                    }

                    is TodoItemsListUiState.ListState.Error -> {
                        ErrorComponent(
                            exception = uiState.listState.exception,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(paddingValue)
                        )
                    }

                    TodoItemsListUiState.ListState.Loading -> {
                        LoadingComponent(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValue)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ListLoadedContent(
    lazyListState: LazyListState,
    listState: TodoItemsListUiState.ListState.Loaded,
    onChecked: (TodoItem, Boolean) -> Unit,
    onDelete: (TodoItem) -> Unit,
    toEditItemScreen: (itemId: String?) -> Unit,
    modifier: Modifier = Modifier,
    bottomPadding: Dp = 0.dp,
) {
    LazyColumn(
        modifier = modifier,
        userScrollEnabled = true,
        state = lazyListState
    ) {
        item {
            Spacer(modifier = Modifier.height(AppTheme.dimensions.paddingSmall))
        }
        item {
            val shape = RoundedCornerShape(
                topEnd = AppTheme.dimensions.cardCornersRadius,
                topStart = AppTheme.dimensions.cardCornersRadius
            )
            BoxWithSidesForShadow(
                Sides.TOP,
            ) {
                Spacer(
                    modifier = Modifier
                        .shadow(AppTheme.dimensions.shadowElevationSmall, shape)
                        .clip(shape)
                        .background(AppTheme.colors.secondaryBack)
                        .fillMaxWidth()
                        .height(AppTheme.dimensions.paddingMedium)
                )
            }
        }
        items(
            listState.items.size,
            key = { i -> listState.items[i].id },
        ) {
            val item = listState.items[it]
            TodoItemRow(
                item = item,
                onChecked = { value -> onChecked(item, value) },
                onDeleted = { onDelete(item) },
                onInfoClicked = { toEditItemScreen(item.id) },
                dismissOnCheck = listState.filterState == TodoItemsListUiState.ListState.FilterState.NOT_COMPLETED,
                modifier = Modifier.animateItem(
                    fadeOutSpec = tween(
                        durationMillis = AppTheme.dimensions.animationDuration,
                    ),
                    fadeInSpec = tween(
                        durationMillis = AppTheme.dimensions.animationDuration,
                    ),
                )
            )
        }
        item(key = -1) {
            BoxWithSidesForShadow(
                Sides.LEFT_AND_RIGHT,
                modifier = Modifier
                    .animateItem(
                        fadeOutSpec = tween(
                            durationMillis = AppTheme.dimensions.animationDuration,
                        ),
                        fadeInSpec = tween(
                            durationMillis = AppTheme.dimensions.animationDuration,
                        ),
                    )
            ) {
                Row(
                    modifier = Modifier
                        .defaultMinSize(minHeight = AppTheme.dimensions.sizeItemMinHeight)
                        .fillMaxWidth()
                        .shadow(AppTheme.dimensions.shadowElevationSmall)
                        .background(AppTheme.colors.secondaryBack)
                        .clickable { toEditItemScreen(null) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(AppTheme.dimensions.paddingExtraExtraLarge))
                    Text(
                        text = stringResource(id = R.string.new_item),
                        modifier = Modifier.padding(AppTheme.dimensions.paddingLarge),
                        color = AppTheme.colors.secondary,
                        style = AppTheme.typography.body
                    )
                }
            }
        }
        item(key = -2) {
            val shape = RoundedCornerShape(
                bottomEnd = AppTheme.dimensions.cardCornersRadius,
                bottomStart = AppTheme.dimensions.cardCornersRadius
            )
            BoxWithSidesForShadow(
                Sides.BOTTOM,
                modifier = Modifier
                    .animateItem(
                        fadeOutSpec = tween(
                            durationMillis = AppTheme.dimensions.animationDuration,
                        ),
                        fadeInSpec = tween(
                            durationMillis = AppTheme.dimensions.animationDuration,
                        ),
                    )
            ) {
                Spacer(
                    modifier = Modifier
                        .padding(bottom = AppTheme.dimensions.paddingSmall)
                        .shadow(AppTheme.dimensions.shadowElevationSmall, shape)
                        .clip(shape)
                        .background(AppTheme.colors.secondaryBack)
                        .fillMaxWidth()
                        .height(AppTheme.dimensions.paddingMedium)
                )
            }
        }
        item(key = -3) {
            Spacer(
                modifier = Modifier
                    .animateItem(
                        fadeOutSpec = tween(
                            durationMillis = AppTheme.dimensions.animationDuration,
                        ),
                        fadeInSpec = tween(
                            durationMillis = AppTheme.dimensions.animationDuration,
                        ),
                    )
                    .height(
                        AppTheme.dimensions.paddingExtraExtraLarge +
                                bottomPadding
                    )
            )
        }
    }
}

@DefaultPreview
@ThemePreviews
@LayoutDirectionPreviews
@LanguagePreviews
@FontScalePreviews
@OrientationPreviews
@Composable
private fun TodoListItemScreenPreview(
    @PreviewParameter(TodoItemsListUiStatePreviewParameterProvider::class) state: TodoItemsListUiState
) {
    ScreenPreviewTemplate {
        TodoItemsListScreenContent(
            uiState = state,
            toEditItemScreen = {},
            onFilterChange = {},
            onChecked = { _, _ -> },
            onDelete = {},
            onRefresh = {},
            onResetEvent = {},
            onLogin = {},
            onExit = {},
        )
    }
}

private class TodoItemsListUiStatePreviewParameterProvider :
    PreviewParameterProvider<TodoItemsListUiState> {
    override val values: Sequence<TodoItemsListUiState>
        get() = sequenceOf(
            TodoItemsListUiState(
                listState = TodoItemsListUiState.ListState.Loading,
            ),
            TodoItemsListUiState(
                listState = TodoItemsListUiState.ListState.Error(Exception())
            ),
            TodoItemsListUiState(
                listState = TodoItemsListUiState.ListState.Loaded(
                    items = TodoItemPreviewParameterProvider().values.toList(),
                    filterState = TodoItemsListUiState.ListState.FilterState.ALL,
                    doneCount = 0,
                )
            ),
        )
}