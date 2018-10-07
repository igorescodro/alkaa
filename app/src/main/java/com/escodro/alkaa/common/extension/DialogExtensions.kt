package com.escodro.alkaa.common.extension

import androidx.fragment.app.Fragment
import com.escodro.alkaa.common.view.DateTimePickerDialog
import java.util.Calendar

/**
 * Shows a [DateTimePickerDialog], abstracting its construction.
 *
 * @param onDateChanged HOF to receive the user input in [Calendar] format
 */
fun Fragment.showDateTimePicker(onDateChanged: (Calendar) -> Unit) {
    context?.let { context ->
        DateTimePickerDialog(context, onDateChanged).show()
    }
}
