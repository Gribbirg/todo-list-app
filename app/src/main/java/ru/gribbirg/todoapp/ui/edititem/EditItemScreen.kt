package ru.gribbirg.todoapp.ui.edititem

import androidx.compose.animation.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import ru.gribbirg.todoapp.R
import ru.gribbirg.todoapp.ui.components.ErrorComponent
import ru.gribbirg.todoapp.ui.components.LoadingComponent
import ru.gribbirg.todoapp.ui.edititem.components.ItemDeadline
import ru.gribbirg.todoapp.ui.edititem.components.ItemDelete
import ru.gribbirg.todoapp.ui.edititem.components.ItemImportanceSelector
import ru.gribbirg.todoapp.ui.edititem.components.ItemTextField
import ru.gribbirg.todoapp.ui.theme.AppTheme

@Serializable
data class EditItem(
    val itemId: String?
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditItemScreen(
    itemId: String?,
    viewModel: EditItemViewModel,
    onClose: () -> Unit
) {
    viewModel.setItem(itemId)

    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val systemUiController = rememberSystemUiController()
    val focusManager = LocalFocusManager.current

    val appBarColor = AppTheme.colors.primaryBack
    val scrolledAppBarColor = AppTheme.colors.appBar

    val topColor = remember {
        Animatable(if (scrollState.canScrollBackward) appBarColor else scrolledAppBarColor)
    }

    val topElevation = remember {
        androidx.compose.animation.core.Animatable(if (scrollState.canScrollBackward) 10f else 0f)
    }

    LaunchedEffect(scrollState.canScrollBackward) {
        launch { topElevation.animateTo(if (scrollState.canScrollBackward) 10f else 0f) }
        launch { topColor.animateTo(if (scrollState.canScrollBackward) scrolledAppBarColor else appBarColor) }
    }

    systemUiController.setStatusBarColor(topColor.value)

    Scaffold(
        containerColor = AppTheme.colors.primaryBack,
        contentColor = AppTheme.colors.primary,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = {
                                focusManager.clearFocus()
                                viewModel.save()
                                onClose()
                            },
                            enabled = uiState is EditItemUiState.Loaded,
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = AppTheme.colors.blue,
                                disabledContentColor = AppTheme.colors.disable
                            )
                        ) {
                            Text(
                                text = stringResource(id = R.string.save),
                                textAlign = TextAlign.Center,
                                style = AppTheme.typography.button
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                },
                modifier = Modifier
                    .shadow(
                        elevation = topElevation.value.dp
                    )
                    .background(topColor.value),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                ),
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = stringResource(id = R.string.close),
                            tint = AppTheme.colors.primary
                        )
                    }
                }
            )
        }
    ) { paddingValue ->
        when (uiState) {
            is EditItemUiState.Loaded -> {
                Column(
                    modifier = Modifier
                        .padding(
                            top = paddingValue.calculateTopPadding(),
                            start = 16.dp,
                            end = 16.dp
                        )
                        .verticalScroll(scrollState)
                        .focusable()
                ) {
                    val state = uiState as EditItemUiState.Loaded

                    val inputShape = RoundedCornerShape(10.dp)

                    Spacer(modifier = Modifier.height(4.dp))
                    ItemTextField(
                        text = state.item.text,
                        onChanged = { newText ->
                            viewModel.edit(item = state.item.copy(text = newText))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(2.dp, inputShape),
                        shape = inputShape
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ItemImportanceSelector(
                        importance = state.item.importance,
                        onChanged = { importance ->
                            viewModel.edit(state.item.copy(importance = importance))
                        },
                        onClick = focusManager::clearFocus,
                    )
                    EdiItemSeparator(
                        modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                    )
                    ItemDeadline(
                        deadline = state.item.deadline,
                        onChanged = { newDeadline ->
                            viewModel.edit(state.item.copy(deadline = newDeadline))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        onClick = focusManager::clearFocus,
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    EdiItemSeparator(
                        modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                    )
                    ItemDelete(
                        enabled = state.itemState == EditItemUiState.ItemState.EDIT,
                        onDeleted = {
                            viewModel.delete()
                            onClose()
                        },
                        onClick = focusManager::clearFocus,
                    )
                    Spacer(modifier = Modifier.height(paddingValue.calculateBottomPadding()))
                }
            }

            is EditItemUiState.Error -> {
                ErrorComponent(
                    exception = (uiState as EditItemUiState.Error).exception,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(paddingValue)
                )
            }

            EditItemUiState.Loading -> {
                LoadingComponent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValue)
                )
            }
        }
    }
}

@Composable
private fun EdiItemSeparator(
    modifier: Modifier = Modifier
) {
    HorizontalDivider(
        modifier = modifier,
        color = AppTheme.colors.separator
    )
}
