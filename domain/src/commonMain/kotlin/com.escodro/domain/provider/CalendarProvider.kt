package com.escodro.domain.provider

import java.util.Calendar

/**
 * Provide the date and time to be used on the task use cases, respecting the Inversion of Control.
 */
interface CalendarProvider {

    /**
     * Gets the current [Calendar].
     *
     * @return the current [Calendar]
     */
    fun getCurrentCalendar(): Calendar
}
