package ru.gribbirg.todoapp.ui.edititem.components

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import ru.gribbirg.todoapp.R
import ru.gribbirg.todoapp.ui.theme.AppTheme
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ItemDeadlineDatePicker(
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
                Text(text = stringResource(id = R.string.ready), style = AppTheme.typography.button)
            }
        },
        dismissButton = {
            TextButton(
                onClick = close,
                colors = ButtonDefaults.textButtonColors(contentColor = AppTheme.colors.blue)
            ) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    style = AppTheme.typography.button
                )
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
                titleContentColor = AppTheme.colors.tertiary,
                headlineContentColor = AppTheme.colors.primary,
                weekdayContentColor = AppTheme.colors.tertiary,
                subheadContentColor = Color.Unspecified,
                navigationContentColor = AppTheme.colors.primary,
                yearContentColor = AppTheme.colors.primary,
                disabledYearContentColor = Color.Unspecified,
                currentYearContentColor = AppTheme.colors.blue,
                selectedYearContentColor = AppTheme.colors.white,
                disabledSelectedYearContentColor = Color.Unspecified,
                selectedYearContainerColor = AppTheme.colors.blue,
                disabledSelectedYearContainerColor = Color.Unspecified,
                dayContentColor = AppTheme.colors.primary,
                disabledDayContentColor = Color.Unspecified,
                selectedDayContentColor = AppTheme.colors.white,
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