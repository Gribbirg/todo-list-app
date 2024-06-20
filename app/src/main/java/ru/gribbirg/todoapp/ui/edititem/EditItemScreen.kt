package ru.gribbirg.todoapp.ui.edititem

import androidx.compose.animation.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import ru.gribbirg.todoapp.R
import ru.gribbirg.todoapp.data.data.TodoImportance
import ru.gribbirg.todoapp.ui.theme.AppTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditItemScreen(
    viewModel: EditItemViewModel,
    onClose: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val systemUiController = rememberSystemUiController()

    val appBarColor = AppTheme.colors.primaryBack
    val scrolledAppBarColor = if (isSystemInDarkTheme())
        AppTheme.colors.secondaryBack
    else
        AppTheme.colors.primaryBack
    val topColor = remember {
        Animatable(if (scrollState.canScrollBackward) appBarColor else scrolledAppBarColor)
    }

    val topElevation = remember {
        androidx.compose.animation.core.Animatable(if (scrollState.canScrollBackward) 30f else 0f)
    }

    LaunchedEffect(scrollState.canScrollBackward) {
        launch { topElevation.animateTo(if (scrollState.canScrollBackward) 30f else 0f) }
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
                        TextButton(onClick = {
                            viewModel.save()
                            onClose()
                        }) {
                            Text(
                                text = stringResource(id = R.string.save),
                                textAlign = TextAlign.Center,
                                fontSize = 20.sp,
                                color = AppTheme.colors.blue
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
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
                            contentDescription = null,
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

                    ItemTextField(
                        text = state.item.text,
                        onChanged = { newText ->
                            viewModel.edit(item = state.item.copy(text = newText))
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ItemImportanceSelector(
                        importance = state.item.importance,
                        onChanged = { importance ->
                            viewModel.edit(state.item.copy(importance = importance))
                        }
                    )
                    EdiItemSeparator(
                        modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                    )
                    ItemDeadline(
                        deadline = state.item.deadline,
                        onChanged = { newDeadline ->
                            viewModel.edit(state.item.copy(deadline = newDeadline))
                        },
                        modifier = Modifier.fillMaxWidth()
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
                        }
                    )
                }
            }

            else -> {
                CircularProgressIndicator()
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

@Composable
private fun ItemTextField(
    text: String,
    onChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val indicatorColor = Color.Transparent
    val containerColor = AppTheme.colors.secondaryBack
    val textColor = AppTheme.colors.primary
    val placeFolderColor = AppTheme.colors.gray
    val cursorColor = AppTheme.colors.blue
    TextField(
        value = text,
        onValueChange = { onChanged(it) },
        modifier = modifier,
        minLines = 5,
        placeholder = { Text(text = stringResource(id = R.string.type_text)) },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = indicatorColor,
            unfocusedIndicatorColor = indicatorColor,
            disabledIndicatorColor = indicatorColor,
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            disabledContainerColor = containerColor,
            focusedTextColor = textColor,
            unfocusedTextColor = textColor,
            disabledTextColor = textColor,
            focusedPlaceholderColor = placeFolderColor,
            disabledPlaceholderColor = placeFolderColor,
            unfocusedPlaceholderColor = placeFolderColor,
            cursorColor = cursorColor
        ),
        shape = RoundedCornerShape(10.dp)
    )
}

@Composable
private fun ItemImportanceSelector(
    importance: TodoImportance,
    onChanged: (TodoImportance) -> Unit,
    modifier: Modifier = Modifier
) {
    var menuOpened by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(id = R.string.importance),
            modifier = Modifier.padding(start = 8.dp)
        )
        TextButton(
            onClick = { menuOpened = true },
            colors = ButtonDefaults.textButtonColors(
                contentColor = importance.colorId?.let { colorResource(it) }
                    ?: AppTheme.colors.gray
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (importance.logoId != null) {
                    Icon(
                        painter = painterResource(id = importance.logoId),
                        contentDescription = null,
                        modifier = Modifier,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(text = stringResource(id = importance.resourceId), fontSize = 16.sp)
            }
        }

        DropdownMenu(
            expanded = menuOpened,
            onDismissRequest = { menuOpened = false },
            modifier = Modifier.background(AppTheme.colors.secondaryBack)
        ) {
            for (importanceValue in TodoImportance.entries) {
                val color = importanceValue.colorId?.let { colorResource(it) }
                    ?: AppTheme.colors.gray
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(id = importanceValue.resourceId),
                        )
                    },
                    onClick = {
                        onChanged(importanceValue)
                        menuOpened = false
                    },
                    leadingIcon = importanceValue.logoId?.let {
                        {
                            Icon(
                                painterResource(id = it),
                                contentDescription = null
                            )
                        }
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = color,
                        leadingIconColor = color
                    )
                )
            }
        }
    }
}

@Composable
private fun ItemDeadline(
    deadline: LocalDate?,
    onChanged: (LocalDate?) -> Unit,
    modifier: Modifier = Modifier
) {
    var dialogOpened by remember {
        mutableStateOf(false)
    }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = stringResource(id = R.string.deadline),
                modifier = Modifier.padding(start = 8.dp)
            )
            TextButton(
                onClick = { dialogOpened = true },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = AppTheme.colors.blue
                )
            ) {
                if (deadline != null) {
                    Text(
                        text = stringResource(
                            id = R.string.day_month_year_date_template,
                            deadline.dayOfMonth,
                            stringArrayResource(id = R.array.months_names)[deadline.monthValue - 1],
                            deadline.year
                        )
                    )
                }
            }
        }
        Switch(
            checked = deadline != null,
            onCheckedChange = { onChanged(if (it) LocalDate.now() else null) },
            colors = SwitchDefaults.colors(
                checkedThumbColor = AppTheme.colors.white,
                checkedTrackColor = AppTheme.colors.blue,
                checkedBorderColor = Color.Transparent,
                uncheckedThumbColor = AppTheme.colors.gray,
                uncheckedTrackColor = AppTheme.colors.grayLight,
                uncheckedBorderColor = AppTheme.colors.gray
            )
        )
    }

    if (dialogOpened) {
        ItemDeadlineDatePicker(
            startingValue = deadline!!,
            onChanged = onChanged,
            close = { dialogOpened = false })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ItemDeadlineDatePicker(
    startingValue: LocalDate,
    onChanged: (LocalDate?) -> Unit,
    close: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    datePickerState.selectedDateMillis =
        startingValue.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    DatePickerDialog(
        onDismissRequest = close,
        confirmButton = {
            TextButton(
                onClick = {
                    onChanged(
                        Instant
                            .ofEpochMilli(datePickerState.selectedDateMillis!!)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    )
                    close()
                },
                enabled = datePickerState.selectedDateMillis != null,
                colors = ButtonDefaults.textButtonColors(contentColor = AppTheme.colors.blue)
            ) {
                Text(text = stringResource(id = R.string.ready))
            }
        },
        dismissButton = {
            TextButton(
                onClick = close,
                colors = ButtonDefaults.textButtonColors(contentColor = AppTheme.colors.blue)
            ) {
                Text(text = stringResource(id = R.string.cancel))
            }
        },
        colors = DatePickerDefaults.colors(
            containerColor = AppTheme.colors.secondaryBack
        )
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                containerColor = AppTheme.colors.secondaryBack,
                titleContentColor = AppTheme.colors.gray,
                headlineContentColor = AppTheme.colors.primary,
                weekdayContentColor = AppTheme.colors.gray,
                subheadContentColor = Color.Unspecified,
                navigationContentColor = AppTheme.colors.gray,
                yearContentColor = AppTheme.colors.primary,
                disabledYearContentColor = Color.Unspecified,
                currentYearContentColor = AppTheme.colors.blue,
                selectedYearContentColor = AppTheme.colors.primary,
                disabledSelectedYearContentColor = Color.Unspecified,
                selectedYearContainerColor = AppTheme.colors.blue,
                disabledSelectedYearContainerColor = Color.Unspecified,
                dayContentColor = AppTheme.colors.primary,
                disabledDayContentColor = Color.Unspecified,
                selectedDayContentColor = AppTheme.colors.primary,
                disabledSelectedDayContentColor = Color.Unspecified,
                selectedDayContainerColor = AppTheme.colors.blue,
                disabledSelectedDayContainerColor = Color.Unspecified,
                todayContentColor = AppTheme.colors.blue,
                todayDateBorderColor = Color.Transparent,
                dividerColor = AppTheme.colors.separator,
                dateTextFieldColors = TextFieldDefaults.colors(
                    focusedTextColor = AppTheme.colors.primary,
                    unfocusedTextColor = AppTheme.colors.primary,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = AppTheme.colors.blue,
                    selectionColors = null,
                    focusedIndicatorColor = AppTheme.colors.blue,
                    unfocusedIndicatorColor = AppTheme.colors.gray,
                    focusedLabelColor = AppTheme.colors.blue,
                    unfocusedLabelColor = AppTheme.colors.gray,
                    errorCursorColor = AppTheme.colors.red,
                    errorLabelColor = AppTheme.colors.red,
                    errorContainerColor = AppTheme.colors.secondaryBack,
                    errorIndicatorColor = AppTheme.colors.red,
                    errorTextColor = AppTheme.colors.primary
                )
            )
        )
    }
}

@Composable
private fun ItemDelete(enabled: Boolean, onDeleted: () -> Unit, modifier: Modifier = Modifier) {
    TextButton(
        onClick = onDeleted,
        modifier = modifier,
        colors = ButtonDefaults.textButtonColors(contentColor = AppTheme.colors.red),
        enabled = enabled
    ) {
        Icon(Icons.Filled.Delete, contentDescription = null)
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = stringResource(id = R.string.delelte))
    }
}