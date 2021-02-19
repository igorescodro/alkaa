package com.escodro.domain.usecase.alarm

import java.util.Calendar

/**
 * Use case to schedule a new alarm.
 */
interface ScheduleAlarm {

    /**
     * Schedules a new alarm.
     *
     * @param taskId the alarm id
     * @param calendar the time to the alarm be scheduled
     */
    suspend operator fun invoke(taskId: Long, calendar: Calendar)
}
