package com.escodro.task.provider

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toNSDate
import platform.Foundation.NSDate
import platform.Foundation.NSRelativeDateTimeFormatter
import platform.Foundation.now
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
internal class IosRelativeDateTimeProvider : RelativeDateTimeProvider {

    override fun toRelativeDateTimeString(dateTime: LocalDateTime): String {
        val dateFormatter = NSRelativeDateTimeFormatter()
        val nsDate = dateTime.toInstant(TimeZone.currentSystemDefault()).toNSDate()
        return dateFormatter.localizedStringForDate(date = nsDate, relativeToDate = NSDate.now)
    }
}
