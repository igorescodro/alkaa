package com.escodro.task.presentation.detail.alarm

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.escodro.resources.Res
import com.escodro.resources.dialog_picker_confirm
import com.escodro.resources.dialog_picker_next
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource

/**
 * Composable to show a date and time picker.
 *
 * @param isDialogOpen if the dialog should be open
 * @param onCloseDialog callback called when the dialog is closed
 * @param onDateChanged callback called when the date is changed
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimerPicker(
    isDialogOpen: Boolean,
    onCloseDialog: () -> Unit,
    onDateChanged: (LocalDateTime) -> Unit,
) {
    val now = Clock.System.now()
    val displayTime = now.plus(
        value = 1,
        unit = DateTimeUnit.HOUR,
        timeZone = TimeZone.currentSystemDefault(),
    ).toLocalDateTime(TimeZone.currentSystemDefault())

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = now.toEpochMilliseconds(),
    )
    val timePickerState = rememberTimePickerState(
        initialHour = displayTime.hour,
        initialMinute = 0,
    )
    var dialogState by remember(isDialogOpen) { mutableStateOf(DateTimePickerState.DATE) }

    if (!isDialogOpen) {
        return
    }

    if (dialogState == DateTimePickerState.DONE) {
        val date = Instant
            .fromEpochMilliseconds(datePickerState.selectedDateMillis ?: 0)
            .toLocalDateTime(TimeZone.UTC) // Don't apply timezone in the date picked by the user

        val localDateTime = LocalDateTime(
            year = date.year,
            monthNumber = date.monthNumber,
            dayOfMonth = date.dayOfMonth,
            hour = timePickerState.hour,
            minute = timePickerState.minute,
        )
        onDateChanged(localDateTime)
        onCloseDialog()
    }

    if (dialogState == DateTimePickerState.DATE) {
        DatePickerDialog(
            onDismissRequest = onCloseDialog,
            confirmButton = {
                Button(onClick = {
                    dialogState = DateTimePickerState.TIME
                }) {
                    Text(text = stringResource(Res.string.dialog_picker_next))
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (dialogState == DateTimePickerState.TIME) {
        AlertDialog(
            onDismissRequest = onCloseDialog,
            confirmButton = {
                Button(onClick = {
                    dialogState = DateTimePickerState.DONE
                }) {
                    Text(text = stringResource(Res.string.dialog_picker_confirm))
                }
            },
            text = {
                TimePicker(state = timePickerState)
            },
        )
    }
}

/**
 * Enum class to represent the state of the [DateTimerPicker].
 */
private enum class DateTimePickerState {

    /**
     * Date picker dialog should be shown.
     */
    DATE,

    /**
     * Time picker dialog should be shown.
     */
    TIME,

    /**
     * Selected date and time should be returned.
     */
    DONE,
}
