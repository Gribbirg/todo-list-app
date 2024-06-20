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
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
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
import androidx.compose.ui.platform.LocalFocusManager
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
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditItemScreen(
    viewModel: EditItemViewModel,
    onClose: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val systemUiController = rememberSystemUiController()

    val appBarColor = MaterialTheme.colorScheme.background
    val scrolledAppBarColor = if (isSystemInDarkTheme())
        MaterialTheme.colorScheme.surface
    else
        MaterialTheme.colorScheme.background
    val topColor = remember {
        Animatable(if (scrollState.canScrollBackward) appBarColor else scrolledAppBarColor)
    }

    val topElevation = remember {
        androidx.compose.animation.core.Animatable(if (scrollState.canScrollBackward) 30f else 0f)
    }

    val focusManager = LocalFocusManager.current

    LaunchedEffect(scrollState.canScrollBackward) {
        launch { topElevation.animateTo(if (scrollState.canScrollBackward) 30f else 0f) }
        launch { topColor.animateTo(if (scrollState.canScrollBackward) scrolledAppBarColor else appBarColor) }
    }

    systemUiController.setStatusBarColor(topColor.value)

    Scaffold(
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
                                color = MaterialTheme.colorScheme.primary
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
                        Icon(Icons.Filled.Close, contentDescription = null)
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
                    HorizontalDivider(modifier = Modifier.padding(top = 8.dp, bottom = 16.dp))
                    ItemDeadline(
                        deadline = state.item.deadline,
                        onChanged = { newDeadline ->
                            viewModel.edit(state.item.copy(deadline = newDeadline))
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    HorizontalDivider(modifier = Modifier.padding(top = 8.dp, bottom = 16.dp))
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
private fun ItemTextField(
    text: String,
    onChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = text,
        onValueChange = { onChanged(it) },
        modifier = modifier,
        minLines = 5,
        placeholder = { Text(text = stringResource(id = R.string.type_text)) },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
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
                    ?: MaterialTheme.colorScheme.onSurface
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
            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
        ) {
            for (importanceValue in TodoImportance.entries) {
                val color = importanceValue.colorId?.let { colorResource(it) }
                    ?: MaterialTheme.colorScheme.onSurface
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

@OptIn(ExperimentalMaterial3Api::class)
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
            TextButton(onClick = { dialogOpened = true }) {
                if (deadline != null) {
                    Text(
                        text = stringResource(
                            id = R.string.day_month_date_template,
                            deadline.dayOfMonth,
                            stringArrayResource(id = R.array.months_names)[deadline.monthValue]
                        )
                    )
                }
            }
        }
        Switch(
            checked = deadline != null,
            onCheckedChange = { onChanged(if (it) LocalDate.now() else null) })
    }

    if (dialogOpened) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { dialogOpened = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        onChanged(
                            Instant
                                .ofEpochMilli(datePickerState.selectedDateMillis!!)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        )
                        dialogOpened = false
                    },
                    enabled = datePickerState.selectedDateMillis != null
                ) {
                    Text(text = stringResource(id = R.string.ready))
                }
            },
            dismissButton = {
                TextButton(onClick = { dialogOpened = false }) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
private fun ItemDelete(enabled: Boolean, onDeleted: () -> Unit, modifier: Modifier = Modifier) {
    TextButton(
        onClick = onDeleted,
        modifier = modifier,
        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
        enabled = enabled
    ) {
        Icon(Icons.Filled.Delete, contentDescription = null)
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = stringResource(id = R.string.delelte))
    }
}