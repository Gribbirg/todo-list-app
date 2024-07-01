package ru.gribbirg.todoapp.ui.todoitemslist

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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.gribbirg.todoapp.R
import ru.gribbirg.todoapp.data.data.TodoItem
import ru.gribbirg.todoapp.ui.components.ErrorComponent
import ru.gribbirg.todoapp.ui.components.LoadingComponent
import ru.gribbirg.todoapp.ui.previews.DefaultPreview
import ru.gribbirg.todoapp.ui.previews.FontScalePreviews
import ru.gribbirg.todoapp.ui.previews.LanguagePreviews
import ru.gribbirg.todoapp.ui.previews.LayoutDirectionPreviews
import ru.gribbirg.todoapp.ui.previews.OrientationPreviews
import ru.gribbirg.todoapp.ui.previews.ScreenPreviewTemplate
import ru.gribbirg.todoapp.ui.previews.ThemePreviews
import ru.gribbirg.todoapp.ui.previews.TodoItemPreviewParameterProvider
import ru.gribbirg.todoapp.ui.theme.AppTheme
import ru.gribbirg.todoapp.ui.todoitemslist.components.BoxWithSidesForShadow
import ru.gribbirg.todoapp.ui.todoitemslist.components.Sides
import ru.gribbirg.todoapp.ui.todoitemslist.components.TodoItemListCollapsingToolbar
import ru.gribbirg.todoapp.ui.todoitemslist.components.TodoItemRow

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
        onDelete = viewModel::delete
    )
}

@Composable
private fun TodoItemsListScreenContent(
    uiState: TodoItemsListUiState,
    toEditItemScreen: (itemId: String?) -> Unit,
    onFilterChange: (TodoItemsListUiState.FilterState) -> Unit,
    onChecked: (TodoItem, Boolean) -> Unit,
    onDelete: (TodoItem) -> Unit,
) {
    val lazyListState = rememberLazyListState()

    Scaffold(
        containerColor = AppTheme.colors.primaryBack,
        floatingActionButton = {
            if (uiState is TodoItemsListUiState.Loaded)
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
        TodoItemListCollapsingToolbar(
            topPadding = paddingValue.calculateTopPadding(),
            doneCount = (uiState as? TodoItemsListUiState.Loaded)?.doneCount,
            filterState = (uiState as? TodoItemsListUiState.Loaded)?.filterState,
            onFilterChange = onFilterChange
        ) {
            when (uiState) {
                is TodoItemsListUiState.Loaded -> {
                    LazyColumn(
                        modifier = Modifier
                            .animateContentSize()
                            .fillMaxWidth()
                            .padding(
                                start = AppTheme.dimensions.paddingMedium,
                                end = AppTheme.dimensions.paddingMedium,
                            ),
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
                        items(uiState.items.size, key = { i -> uiState.items[i].hashCode() }) {
                            val item = uiState.items[it]
                            TodoItemRow(
                                item = item,
                                onChecked = { value -> onChecked(item, value) },
                                onDeleted = { onDelete(item) },
                                onInfoClicked = { toEditItemScreen(item.id) },
                                dismissOnCheck = uiState.filterState == TodoItemsListUiState.FilterState.NOT_COMPLETED,
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
                                                paddingValue.calculateBottomPadding()
                                    )
                            )
                        }
                    }
                }

                is TodoItemsListUiState.Error -> {
                    ErrorComponent(
                        exception = uiState.exception,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(paddingValue)
                    )
                }

                TodoItemsListUiState.Loading -> {
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
            onDelete = {}
        )
    }
}

private class TodoItemsListUiStatePreviewParameterProvider :
    PreviewParameterProvider<TodoItemsListUiState> {
    override val values: Sequence<TodoItemsListUiState>
        get() = sequenceOf(
            TodoItemsListUiState.Loading,
            TodoItemsListUiState.Error(Exception()),
            TodoItemsListUiState.Loaded(
                items = TodoItemPreviewParameterProvider().values.toList(),
                filterState = TodoItemsListUiState.FilterState.ALL,
                doneCount = 0
            ),
        )
}