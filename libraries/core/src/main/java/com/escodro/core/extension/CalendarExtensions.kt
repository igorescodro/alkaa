package com.escodro.core.extension

import java.text.DateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Formats the [Calendar] based on the current [Locale] set in the device.
 *
 * @return the formatted date
 */
fun Calendar.format(): String {
    val dateFormat = DateFormat.getDateTimeInstance(
        DateFormat.LONG,
        DateFormat.SHORT,
        Locale.getDefault()
    )
    return dateFormat.format(time)
}
