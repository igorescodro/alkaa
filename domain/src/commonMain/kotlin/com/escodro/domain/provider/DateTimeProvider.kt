package com.escodro.domain.provider

import kotlinx.datetime.LocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Provide the date and time to be used on the task use cases, respecting the Inversion of Control.
 */
@OptIn(ExperimentalTime::class)
interface DateTimeProvider {

    /**
     * Gets the current [Instant].
     *
     * @return the current [Instant]
     */
    fun getCurrentInstant(): Instant

    fun getCurrentLocalDateTime(): LocalDateTime
}
