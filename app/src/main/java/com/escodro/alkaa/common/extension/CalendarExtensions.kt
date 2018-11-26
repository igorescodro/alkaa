package com.escodro.alkaa.common.extension

import android.content.Context
import android.text.format.DateUtils
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

/**
 * Formats the time with a relative time presentation, showing _TODAY_ if the date is set to the
 * same day of the device.
 *
 * @param time the time in millis
 *
 * @return the formatted date
 */
fun Context.formatRelativeDate(time: Long?): String {
    if (time == null) {
        return ""
    }

    return DateUtils
        .getRelativeDateTimeString(this, time, DateUtils.DAY_IN_MILLIS, DateUtils.DAY_IN_MILLIS, 0)
        .toString()
}
