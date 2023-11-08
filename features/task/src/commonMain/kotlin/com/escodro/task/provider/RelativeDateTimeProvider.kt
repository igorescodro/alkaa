package com.escodro.task.provider

import kotlinx.datetime.LocalDateTime

/**
 * Provides the relative date time string using the platform implementation.
 */
internal interface RelativeDateTimeProvider {

    /**
     * Converts the [dateTime] to a relative date time string.
     *
     * @param dateTime the date time to be converted
     *
     * @return the relative date time string
     */
    fun toRelativeDateTimeString(dateTime: LocalDateTime): String
}
