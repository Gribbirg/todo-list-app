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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import ru.gribbirg.todoapp.R
import ru.gribbirg.todoapp.data.data.TodoItem
import ru.gribbirg.todoapp.ui.components.ErrorComponent
import ru.gribbirg.todoapp.ui.components.LoadingComponent
import ru.gribbirg.todoapp.ui.edititem.components.ItemDeadline
import ru.gribbirg.todoapp.ui.edititem.components.ItemDelete
import ru.gribbirg.todoapp.ui.edititem.components.ItemImportanceSelector
import ru.gribbirg.todoapp.ui.edititem.components.ItemTextField
import ru.gribbirg.todoapp.ui.previews.DefaultPreview
import ru.gribbirg.todoapp.ui.previews.FontScalePreviews
import ru.gribbirg.todoapp.ui.previews.LanguagePreviews
import ru.gribbirg.todoapp.ui.previews.LayoutDirectionPreviews
import ru.gribbirg.todoapp.ui.previews.OrientationPreviews
import ru.gribbirg.todoapp.ui.previews.ScreenPreviewTemplate
import ru.gribbirg.todoapp.ui.previews.ThemePreviews
import ru.gribbirg.todoapp.ui.theme.AppTheme

@Composable
fun EditItemScreen(
    viewModel: EditItemViewModel = viewModel(factory = EditItemViewModel.Factory),
    onClose: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    EditItemScreenContent(
        uiState = uiState,
        onClose = onClose,
        onSave = viewModel::save,
        onEdit = viewModel::edit,
        onDelete = viewModel::delete
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun EditItemScreenContent(
    uiState: EditItemUiState,
    onClose: () -> Unit,
    onSave: () -> Unit,
    onEdit: (TodoItem) -> Unit,
    onDelete: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val systemUiController = rememberSystemUiController()
    val focusManager = LocalFocusManager.current

    val appBarColor = AppTheme.colors.primaryBack
    val scrolledAppBarColor = AppTheme.colors.appBar

    val topColor = remember {
        Animatable(if (scrollState.canScrollBackward) appBarColor else scrolledAppBarColor)
    }

    val elevationNo = AppTheme.dimensions.shadowElevationNo.value
    val elevationFull = AppTheme.dimensions.shadowElevationLarge.value

    val topElevation = remember {
        androidx.compose.animation.core.Animatable(if (scrollState.canScrollBackward) elevationFull else elevationNo)
    }

    LaunchedEffect(scrollState.canScrollBackward) {
        launch { topElevation.animateTo(if (scrollState.canScrollBackward) elevationFull else elevationNo) }
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
                                onSave()
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
                        Spacer(modifier = Modifier.width(AppTheme.dimensions.paddingSmall))
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
                            start = AppTheme.dimensions.paddingScreenMedium,
                            end = AppTheme.dimensions.paddingScreenMedium
                        )
                        .verticalScroll(scrollState)
                        .focusable()
                ) {
                    val inputShape = RoundedCornerShape(AppTheme.dimensions.cardCornersRadius)

                    Spacer(modifier = Modifier.height(AppTheme.dimensions.paddingSmall))
                    ItemTextField(
                        text = uiState.item.text,
                        onChanged = { newText ->
                            onEdit(uiState.item.copy(text = newText))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(AppTheme.dimensions.shadowElevationSmall, inputShape),
                        shape = inputShape
                    )
                    Spacer(modifier = Modifier.height(AppTheme.dimensions.paddingLarge))
                    ItemImportanceSelector(
                        importance = uiState.item.importance,
                        onChanged = { importance ->
                            onEdit(uiState.item.copy(importance = importance))
                        },
                        onClick = focusManager::clearFocus,
                    )
                    EdiItemSeparator(
                        modifier = Modifier.padding(
                            top = AppTheme.dimensions.paddingMedium,
                            bottom = AppTheme.dimensions.paddingLarge
                        )
                    )
                    ItemDeadline(
                        deadline = uiState.item.deadline,
                        onChanged = { newDeadline ->
                            onEdit(uiState.item.copy(deadline = newDeadline))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        onClick = focusManager::clearFocus,
                    )
                    Spacer(modifier = Modifier.height(AppTheme.dimensions.paddingExtraLarge))
                    EdiItemSeparator(
                        modifier = Modifier.padding(
                            top = AppTheme.dimensions.paddingMedium,
                            bottom = AppTheme.dimensions.paddingLarge
                        )
                    )
                    ItemDelete(
                        enabled = uiState.itemState == EditItemUiState.ItemState.EDIT,
                        onDeleted = {
                            onDelete()
                            onClose()
                        },
                        onClick = focusManager::clearFocus,
                    )
                    Spacer(modifier = Modifier.height(paddingValue.calculateBottomPadding()))
                }
            }

            is EditItemUiState.Error -> {
                ErrorComponent(
                    exception = uiState.exception,
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

            EditItemUiState.Saving -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValue),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LoadingComponent()
                    Spacer(modifier = Modifier.height(AppTheme.dimensions.paddingLarge))
                    Text(
                        text = stringResource(R.string.saving),
                        style = AppTheme.typography.body,
                        color = AppTheme.colors.blue,
                        textAlign = TextAlign.Center
                    )
                }
            }

            EditItemUiState.Finish -> {
                LaunchedEffect(Unit) {
                    onClose()
                }
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

@DefaultPreview
@ThemePreviews
@LanguagePreviews
@LayoutDirectionPreviews
@FontScalePreviews
@OrientationPreviews
@Composable
private fun EditItemScreenPreview(
    @PreviewParameter(EditItemUiStatePreviewProvider::class) uiState: EditItemUiState
) {
    ScreenPreviewTemplate {
        var state by remember {
            mutableStateOf(
                uiState
            )
        }
        EditItemScreenContent(
            uiState = state,
            onClose = { },
            onSave = { },
            onEdit = {
                if (state is EditItemUiState.Loaded) state =
                    (state as EditItemUiState.Loaded).copy(item = it)
            },
            onDelete = { },
        )
    }
}

private class EditItemUiStatePreviewProvider : PreviewParameterProvider<EditItemUiState> {
    override val values: Sequence<EditItemUiState> = sequenceOf(
        EditItemUiState.Loading,
        EditItemUiState.Saving,
        EditItemUiState.Error(Exception()),
        EditItemUiState.Loaded(TodoItem(), EditItemUiState.ItemState.EDIT),
        EditItemUiState.Loaded(
            TodoItem(
                text = "Фьючерсный контракт — это договор между покупателем и продавцом о покупке/продаже какого-то актива в будущем. Стороны заранее оговаривают, через какой срок и по какой цене состоится сделка.\n" +
                        "\n" +
                        "Например, сейчас одна акция «Лукойла» стоит около 5700 рублей. Фьючерс на акции «Лукойла» — это, например, договор между покупателем и продавцом о том, что покупатель купит акции «Лукойла» у продавца по цене 5700 рублей через 3 месяца. При этом не важно, какая цена будет у акций через 3 месяца: цена сделки между покупателем и продавцом все равно останется 5700 рублей. Если реальная цена акции через три месяца не останется прежней, одна из сторон в любом случае понесет убытки."
            ), EditItemUiState.ItemState.EDIT
        ),
        EditItemUiState.Loaded(TodoItem(), EditItemUiState.ItemState.NEW)
    )
}
