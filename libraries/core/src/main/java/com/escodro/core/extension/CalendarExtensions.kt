@file:Suppress("Filename")

package com.escodro.core.extension

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
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
        Locale.getDefault(),
    )
    return dateFormat.format(time)
}

/**
 * Converts a [LocalDateTime] to [Calendar] using the current system [TimeZone].
 */
fun LocalDateTime.toCalendar(): Calendar {
    val instant = toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    return Calendar.getInstance().apply { timeInMillis = instant }
}

/**
 * Converts a [Calendar] to [LocalDateTime] using the current system [TimeZone].
 */
fun Calendar.toLocalDateTime(): LocalDateTime {
    val instant = Instant.fromEpochMilliseconds(timeInMillis)
    return instant.toLocalDateTime(TimeZone.currentSystemDefault())
}
