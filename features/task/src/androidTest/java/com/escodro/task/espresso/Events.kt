package com.escodro.task.espresso

import com.adevinta.android.barista.interaction.BaristaPickerInteractions.setDateOnPicker
import com.adevinta.android.barista.interaction.BaristaPickerInteractions.setTimeOnPicker
import com.adevinta.android.barista.interaction.BaristaSleepInteractions
import java.util.Calendar

internal fun setDateTime(calendar: Calendar) {
    with(calendar) {
        BaristaSleepInteractions.sleep(300)
        setDateOnPicker(get(Calendar.YEAR), get(Calendar.MONTH) + 1, get(Calendar.DAY_OF_MONTH))
        BaristaSleepInteractions.sleep(300)
        setTimeOnPicker(get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
    }
}
