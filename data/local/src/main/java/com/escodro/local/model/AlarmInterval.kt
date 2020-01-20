package com.escodro.local.model

/**
 * Represents the interval between repeating intervals.
 */
@Suppress("MagicNumber")
enum class AlarmInterval(val id: Int) {

    HOURLY(1),
    DAILY(2),
    WEEKLY(3),
    MONTHLY(4),
    YEARLY(5)
}
