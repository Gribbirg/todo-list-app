package ru.gribbirg.todoapp.ui.todoitemslist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import ru.gribbirg.todoapp.R
import ru.gribbirg.todoapp.ui.components.ErrorComponent
import ru.gribbirg.todoapp.ui.components.LoadingComponent
import ru.gribbirg.todoapp.ui.theme.AppTheme
import ru.gribbirg.todoapp.ui.todoitemslist.components.BoxWithSidesForShadow
import ru.gribbirg.todoapp.ui.todoitemslist.components.Sides
import ru.gribbirg.todoapp.ui.todoitemslist.components.TodoItemListCollapsingToolbar
import ru.gribbirg.todoapp.ui.todoitemslist.components.TodoItemRow

@Serializable
data object TodoList

@Composable
fun TodoListItemScreen(
    viewModel: TodoItemsListViewModel,
    toEditItemScreen: (itemId: String?) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
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
            onFilterChange = viewModel::onFilterChange
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
                        item {
                            Spacer(modifier = Modifier.height(5.dp))
                        }
                        if (state.items.isNotEmpty()) {
                            item {
                                val shape = RoundedCornerShape(
                                    topEnd = 8.dp,
                                    topStart = 8.dp
                                )
                                BoxWithSidesForShadow(
                                    Sides.TOP,
                                ) {
                                    Spacer(
                                        modifier = Modifier
                                            .shadow(2.dp, shape)
                                            .clip(shape)
                                            .background(AppTheme.colors.secondaryBack)
                                            .fillMaxWidth()
                                            .height(7.dp)
                                    )
                                }
                            }
                            items(state.items.size, key = { i -> state.items[i].hashCode() }) {
                                val item = state.items[it]
                                TodoItemRow(
                                    item = item,
                                    onChecked = { value -> viewModel.onChecked(item, value) },
                                    onDeleted = { viewModel.delete(item) },
                                    onInfoClicked = { toEditItemScreen(item.id) },
                                    dismissOnCheck = state.filterState == TodoItemsListUiState.FilterState.NOT_COMPLETED
                                )
                            }
                            item {
                                val shape = RoundedCornerShape(
                                    bottomEnd = 8.dp,
                                    bottomStart = 8.dp
                                )
                                BoxWithSidesForShadow(
                                    Sides.BOTTOM,
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .shadow(2.dp, shape)
                                            .clip(shape)
                                            .background(AppTheme.colors.secondaryBack)
                                            .clickable { toEditItemScreen(null) }
                                    ) {
                                        Spacer(modifier = Modifier.width(45.dp))
                                        Text(
                                            text = stringResource(id = R.string.new_item),
                                            modifier = Modifier.padding(20.dp),
                                            color = AppTheme.colors.secondary,
                                            style = AppTheme.typography.body
                                        )
                                    }
                                }
                            }
                        } else {
                            item {
                                val shape = RoundedCornerShape(
                                    bottomEnd = 8.dp,
                                    bottomStart = 8.dp,
                                    topEnd = 8.dp,
                                    topStart = 8.dp
                                )
                                Row(
                                    modifier = Modifier
                                        .shadow(2.dp, shape)
                                        .clip(shape)
                                        .background(AppTheme.colors.secondaryBack)
                                        .fillMaxWidth()
                                        .clickable { toEditItemScreen(null) }
                                ) {
                                    Spacer(modifier = Modifier.width(45.dp))
                                    Text(
                                        text = stringResource(id = R.string.new_item),
                                        modifier = Modifier.padding(20.dp),
                                        color = AppTheme.colors.secondary,
                                        style = AppTheme.typography.body
                                    )
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(32.dp))
                        }
                    }
                }

                is TodoItemsListUiState.Error -> {
                    ErrorComponent(
                        exception = (uiState as TodoItemsListUiState.Error).exception,
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