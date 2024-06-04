package com.escodro.task.provider

import android.content.Context
import android.text.format.DateUtils
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

internal class AndroidRelativeDateTimeProvider(
    private val context: Context,
) : RelativeDateTimeProvider {

    override fun toRelativeDateTimeString(dateTime: LocalDateTime): String {
        val time = dateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        return DateUtils
            .getRelativeDateTimeString(
                context,
                time,
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.WEEK_IN_MILLIS,
                0,
            ).toString()
    }
}
