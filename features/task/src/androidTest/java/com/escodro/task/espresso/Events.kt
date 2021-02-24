package com.escodro.task.espresso

import android.R
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matchers
import java.util.Calendar

internal fun setDateTime(calendar: Calendar) {
    setDate(calendar)
    Espresso.onView(ViewMatchers.withId(R.id.button1)).perform(ViewActions.click())
    setTime(calendar)
    Espresso.onView(ViewMatchers.withId(R.id.button1)).perform(ViewActions.click())
}

private fun setDate(calendar: Calendar) {
    Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(DatePicker::class.java.name)))
        .perform(
            PickerActions.setDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        )
}

private fun setTime(calendar: Calendar) {
    Espresso.onView(ViewMatchers.withClassName(Matchers.equalTo(TimePicker::class.java.name)))
        .perform(
            PickerActions.setTime(
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE)
            )
        )
}
