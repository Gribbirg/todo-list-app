package ru.gribbirg.edit

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.gribbirg.domain.model.todo.TodoItem
import ru.gribbirg.edit.components.EditScreenAppBarComponent
import ru.gribbirg.edit.components.ItemDeadline
import ru.gribbirg.edit.components.ItemDelete
import ru.gribbirg.edit.components.ItemImportanceSelector
import ru.gribbirg.edit.components.ItemTextField
import ru.gribbirg.theme.custom.AppTheme
import ru.gribbirg.ui.components.ErrorComponent
import ru.gribbirg.ui.components.LoadingComponent
import ru.gribbirg.ui.previews.DefaultPreview
import ru.gribbirg.ui.previews.FontScalePreviews
import ru.gribbirg.ui.previews.LanguagePreviews
import ru.gribbirg.ui.previews.LayoutDirectionPreviews
import ru.gribbirg.ui.previews.OrientationPreviews
import ru.gribbirg.ui.previews.ScreenPreviewTemplate
import ru.gribbirg.ui.previews.ThemePreviews

/**
 * Edit and create item screen
 *
 * @see EditItemUiState
 * @see EditItemViewModel
 */
@Composable
fun EditItemScreen(
    viewModel: EditItemViewModel,
    onClose: (String?) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    EditItemScreenContent(
        uiState = uiState,
        onClose = onClose,
        onSave = viewModel::save,
        onEdit = viewModel::edit,
    )
}

@Composable
private fun EditItemScreenContent(
    uiState: EditItemUiState,
    onClose: (String?) -> Unit,
    onSave: () -> Unit,
    onEdit: (TodoItem) -> Unit,
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    Scaffold(
        containerColor = AppTheme.colors.primaryBack,
        contentColor = AppTheme.colors.primary,
        topBar = {
            EditScreenAppBarComponent(
                focusManager = focusManager,
                onSave = onSave,
                onClose = { onClose(null) },
                saveEnabled = uiState is EditItemUiState.Loaded,
                scrollState = scrollState,
            )
        }
    ) { paddingValue ->
        when (uiState) {
            is EditItemUiState.Loaded -> {
                EditScreenLoadedContent(
                    uiState = uiState,
                    onEdit = onEdit,
                    focusManager = focusManager,
                    onDelete = { onClose(uiState.item.id) },
                    modifier = Modifier
                        .padding(
                            top = paddingValue.calculateTopPadding(),
                            start = AppTheme.dimensions.paddingScreenMedium,
                            end = AppTheme.dimensions.paddingScreenMedium
                        )
                        .verticalScroll(scrollState)
                        .focusable(),
                    bottomPadding = paddingValue.calculateBottomPadding(),
                )
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
        }
    }
}

@Composable
private fun EditScreenLoadedContent(
    uiState: EditItemUiState.Loaded,
    onEdit: (TodoItem) -> Unit,
    focusManager: FocusManager,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    bottomPadding: Dp = 0.dp,
) {
    Column(
        modifier = modifier
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
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(AppTheme.dimensions.cardCornersRadius))
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
            onDeleted = onDelete,
            onClick = focusManager::clearFocus,
        )
        Spacer(modifier = Modifier.height(bottomPadding))
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
        )
    }
}

private class EditItemUiStatePreviewProvider : PreviewParameterProvider<EditItemUiState> {
    override val values: Sequence<EditItemUiState> = sequenceOf(
        EditItemUiState.Loading,
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
