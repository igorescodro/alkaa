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
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Composable to show a date and time picker.
 *
 * @param initialDateTime a pre-existing value set by user
 * @param isDialogOpen if the dialog should be open
 * @param onCloseDialog callback called when the dialog is closed
 * @param onDateChange callback called when the date is changed
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun DateTimerPicker(
    initialDateTime: LocalDateTime?,
    isDialogOpen: Boolean,
    onCloseDialog: () -> Unit,
    onDateChange: (LocalDateTime) -> Unit,
) {
    val now: Instant = Clock.System.now()
    val displayTime: LocalDateTime = initialDateTime ?: now
        .plus(duration = 1.days)
        .toLocalDateTime(TimeZone.currentSystemDefault())

    val initialSelectedDate = initialDateTime?.toInstant(TimeZone.currentSystemDefault()) ?: now
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialSelectedDate.toEpochMilliseconds(),
    )
    val timePickerState = rememberTimePickerState(
        initialHour = displayTime.hour,
        initialMinute = displayTime.minute,
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
            month = date.month,
            day = date.day,
            hour = timePickerState.hour,
            minute = timePickerState.minute,
        )
        onDateChange(localDateTime)
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
