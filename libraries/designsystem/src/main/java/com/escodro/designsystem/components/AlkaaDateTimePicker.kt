package com.escodro.designsystem.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlkaaDateTimePicker(showDialog: Boolean, onDismiss: () -> Unit, onResult: (Calendar) -> Unit) {
    val context = LocalContext.current
    val calendar by remember { mutableStateOf(Calendar.getInstance()) }

    var openDateDialog by remember { mutableStateOf(true) }
    var openTimeDialog by remember { mutableStateOf(false) }

    if (openDateDialog && showDialog) {
        val datePickerState =
            rememberDatePickerState(initialSelectedDateMillis = calendar.timeInMillis)
        val confirmEnabled =
            remember { derivedStateOf { datePickerState.selectedDateMillis != null } }

        DatePickerDialog(
            onDismissRequest = {
                openDateDialog = false
                onDismiss()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        calendar.apply { timeInMillis = datePickerState.selectedDateMillis ?: 0 }
                        openDateDialog = false
                        openTimeDialog = true
                    },
                    enabled = confirmEnabled.value
                ) {
                    Text(context.getString(android.R.string.ok))
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (openTimeDialog && showDialog) {
        val calendarState = rememberTimePickerState(
            initialHour = calendar.get(Calendar.HOUR_OF_DAY) + 1,
            initialMinute = 0
        )
        DatePickerDialog(
            onDismissRequest = {
                openDateDialog = false
                onDismiss()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        calendar.add(Calendar.HOUR_OF_DAY, calendarState.hour)
                        calendar.add(Calendar.MINUTE, calendarState.minute)
                        onResult(calendar)
                        openTimeDialog = false
                        onDismiss()
                    },
                ) {
                    Text(context.getString(android.R.string.ok))
                }
            },
        ) {
            TimePicker(state = calendarState)
        }
    }
}
