package com.escodro.task.extension

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import java.text.DateFormat
import java.util.Calendar
import java.util.Locale

actual fun LocalDateTime.format(): String {
    val dateFormat = DateFormat.getDateTimeInstance(
        DateFormat.LONG,
        DateFormat.SHORT,
        Locale.getDefault(),
    )
    val calendar = Calendar.getInstance().apply {
        timeInMillis = toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }
    return dateFormat.format(calendar.time)
}
