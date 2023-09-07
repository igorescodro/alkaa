package com.escodro.task.model

import com.escodro.resources.MR
import dev.icerock.moko.resources.StringResource

/**
 * Represents the interval between repeating intervals.
 *
 * @property index the id representation of the interval
 * @property title the title of the interval
 */
@Suppress("MagicNumber")
enum class AlarmInterval(val index: Int?, val title: StringResource) {

    /**
     * Represents no alarm interval.
     */
    NEVER(index = 0, title = MR.strings.task_alarm_repeating_never),

    /**
     * Represents a interval of 1 hour.
     */
    HOURLY(index = 1, title = MR.strings.task_alarm_repeating_hourly),

    /**
     * Represents a interval of 1 day.
     */
    DAILY(index = 2, title = MR.strings.task_alarm_repeating_daily),

    /**
     * Represents a interval of 1 week.
     */
    WEEKLY(index = 3, title = MR.strings.task_alarm_repeating_weekly),

    /**
     * Represents a interval of 1 month.
     */
    MONTHLY(index = 4, title = MR.strings.task_alarm_repeating_monthly),

    /**
     * Represents a interval of 1 year.
     */
    YEARLY(5, title = MR.strings.task_alarm_repeating_yearly),
}
