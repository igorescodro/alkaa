package com.escodro.task.extension

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toNSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSDateFormatterLongStyle
import platform.Foundation.NSDateFormatterShortStyle

actual fun LocalDateTime.format(): String {
    val dateFormatter = NSDateFormatter()
    dateFormatter.dateStyle = NSDateFormatterLongStyle
    dateFormatter.timeStyle = NSDateFormatterShortStyle
    val nsDate = toInstant(TimeZone.currentSystemDefault()).toNSDate()
    return dateFormatter.stringFromDate(nsDate)
}
