package com.escodro.task.model

/**
 * Represents the interval between repeating intervals.
 *
 * @property index the id representation of the interval
 */
@Suppress("MagicNumber")
enum class AlarmInterval(val index: Int?) {

    /**
     * Represents no alarm interval.
     */
    NEVER(0),

    /**
     * Represents a interval of 1 hour.
     */
    HOURLY(1),

    /**
     * Represents a interval of 1 day.
     */
    DAILY(2),

    /**
     * Represents a interval of 1 week.
     */
    WEEKLY(3),

    /**
     * Represents a interval of 1 month.
     */
    MONTHLY(4),

    /**
     * Represents a interval of 1 year.
     */
    YEARLY(5)
}
