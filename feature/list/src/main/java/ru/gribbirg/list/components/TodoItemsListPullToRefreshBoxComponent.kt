package ru.gribbirg.list.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.delay
import ru.gribbirg.ui.previews.DefaultPreview
import ru.gribbirg.ui.previews.ScreenPreviewTemplate
import ru.gribbirg.ui.previews.ThemePreviews
import ru.gribbirg.ui.theme.AppTheme

/**
 * Pull to refresh box
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoItemsListPullToRefreshBox(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val state = rememberPullToRefreshState()
    if (enabled)
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            modifier = modifier,
            state = state,
            indicator = {
                Indicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = AppTheme.dimensions.paddingMedium),
                    isRefreshing = isRefreshing,
                    state = state,
                    containerColor = AppTheme.colors.elevated,
                    color = AppTheme.colors.blue,
                )
            }
        ) {
            content()
        }
    else
        Box(modifier = modifier) {
            content()
        }
}

@DefaultPreview
@ThemePreviews
@Composable
private fun TodoItemsListPullToRefreshBoxPreview() {
    var isRefreshing by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(3000)
            isRefreshing = false
        }
    }

    ScreenPreviewTemplate {
        TodoItemsListPullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { isRefreshing = true },
            enabled = true,
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(100) { i ->
                    Text(
                        text = i.toString(),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = AppTheme.colors.primary
                    )
                }
            }
        }
    }
}