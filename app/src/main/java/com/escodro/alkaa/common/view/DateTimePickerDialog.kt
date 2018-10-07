package com.escodro.alkaa.common.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import java.util.Calendar

/**
 * Custom dialog flow to show the [DatePickerDialog] and [TimePickerDialog] in the same
 * interaction flow.
 */
class DateTimePickerDialog(context: Context, private val onDateChanged: (Calendar) -> Unit) {

    private val calendar = Calendar.getInstance()

    private val datePickerDialog: DatePickerDialog

    private val timePickerDialog: TimePickerDialog

    init {
        datePickerDialog = DatePickerDialog(
            context, getDateListener(),
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        timePickerDialog = TimePickerDialog(
            context,
            getTimeListener(),
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
    }

    /**
     * Starts the date and time dialog picker flow, immediately displaying the dialog.
     */
    fun show() {
        datePickerDialog.show()
    }

    private fun getDateListener() =
        DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            timePickerDialog.show()
        }

    private fun getTimeListener() =
        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            onDateChanged(calendar)
        }
}
