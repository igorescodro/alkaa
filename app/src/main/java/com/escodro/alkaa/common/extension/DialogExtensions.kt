package com.escodro.alkaa.common.extension

import android.app.DatePickerDialog
import androidx.fragment.app.Fragment
import java.util.Calendar

/**
 * Shows a [DatePickerDialog], abstracting its construction.
 *
 * @param onDateChanged HOF to receive the user input in [Calendar] format
 */
fun Fragment.showDatePicker(onDateChanged: (Calendar) -> Unit) {
    context?.let { context ->
        val listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val result = Calendar.getInstance()
            result.set(year, month, dayOfMonth)
            onDateChanged(result)
        }

        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            listener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}
