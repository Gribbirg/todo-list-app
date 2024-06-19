package ru.gribbirg.todoapp.ui.todoitemslist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.gribbirg.todoapp.R
import ru.gribbirg.todoapp.data.data.TodoItem

@Composable
fun TodoListItemScreen(
    viewModel: TodoItemsListViewModel,
    toEditItemScreen: (item: TodoItem?) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val lazyListState = rememberLazyListState()


    Scaffold { paddingValue ->
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
                        if (state.items.isNotEmpty()) {
                            item {
                                Spacer(modifier = Modifier.height(5.dp))
                            }
                            item {
                                TodoItemRow(
                                    item = state.items.first(),
                                    onChecked = viewModel::onChecked,
                                    onInfoClicked = toEditItemScreen,
                                    topBorderRadius = 16.dp
                                )
                            }
                            items(state.items.size - 1) {
                                val item = state.items[it + 1]
                                TodoItemRow(
                                    item = item,
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
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
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