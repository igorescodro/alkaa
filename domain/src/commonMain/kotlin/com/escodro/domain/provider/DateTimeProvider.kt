package com.escodro.domain.provider

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime

/**
 * Provide the date and time to be used on the task use cases, respecting the Inversion of Control.
 */
interface DateTimeProvider {

    /**
     * Gets the current [Instant].
     *
     * @return the current [Instant]
     */
    fun getCurrentInstant(): Instant

    fun getCurrentLocalDateTime(): LocalDateTime
}
