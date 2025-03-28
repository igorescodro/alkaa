package com.escodro.task.provider

import com.escodro.resources.Res
import com.escodro.resources.relative_date_time_days
import com.escodro.resources.relative_date_time_hours
import com.escodro.resources.relative_date_time_just_now
import com.escodro.resources.relative_date_time_minutes
import com.escodro.resources.relative_date_time_one_hour
import com.escodro.resources.relative_date_time_one_minute
import com.escodro.resources.relative_date_time_yesterday
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.jetbrains.compose.resources.getString

internal class DesktopRelativeDateTimeProvider : RelativeDateTimeProvider {

    override fun toRelativeDateTimeString(dateTime: LocalDateTime): String {
        val currentTime = Clock.System.now()
        val targetInstant = dateTime.toInstant(TimeZone.currentSystemDefault())
        val duration = currentTime - targetInstant

        return runBlocking {
            when {
                duration.inWholeMinutes < 1 ->
                    getString(Res.string.relative_date_time_just_now)

                duration.inWholeMinutes == 1L ->
                    getString(Res.string.relative_date_time_one_minute)

                duration.inWholeMinutes < 60 ->
                    getString(Res.string.relative_date_time_minutes, duration.inWholeMinutes)

                duration.inWholeHours == 1L ->
                    getString(Res.string.relative_date_time_one_hour)

                duration.inWholeHours < 24 ->
                    getString(Res.string.relative_date_time_hours, duration.inWholeHours)

                duration.inWholeDays == 1L ->
                    getString(Res.string.relative_date_time_yesterday)

                duration.inWholeDays < 7 ->
                    getString(Res.string.relative_date_time_days, duration.inWholeDays)

                else -> dateTime.toString()
            }
        }
    }
}
