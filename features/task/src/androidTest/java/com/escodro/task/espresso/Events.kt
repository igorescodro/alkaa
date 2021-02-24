package com.escodro.task.espresso

import com.schibsted.spain.barista.interaction.BaristaPickerInteractions.setDateOnPicker
import com.schibsted.spain.barista.interaction.BaristaPickerInteractions.setTimeOnPicker
import java.util.Calendar

internal fun setDateTime(calendar: Calendar) {
    with(calendar) {
        setDateOnPicker(get(Calendar.YEAR), get(Calendar.MONTH) + 1, get(Calendar.DAY_OF_MONTH))
        setTimeOnPicker(get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
    }
}
