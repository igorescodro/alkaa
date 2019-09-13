package com.escodro.domain.calendar

import java.util.Calendar

/**
 * Provide the date and time to be used on the task use cases, respecting the Inversion of Control.
 */
class TaskCalendar {

    /**
     * Gets the current [Calendar].
     *
     * @return the current [Calendar]
     */
    fun getCurrentCalendar(): Calendar = Calendar.getInstance()
}
