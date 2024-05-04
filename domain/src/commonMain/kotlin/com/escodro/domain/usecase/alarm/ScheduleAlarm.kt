package com.escodro.domain.usecase.alarm

import kotlinx.datetime.LocalDateTime

/**
 * Use case to schedule a new alarm.
 */
internal interface ScheduleAlarm {

    /**
     * Schedules a new alarm.
     *
     * @param taskId the alarm id
     * @param localDateTime the time to the alarm be scheduled
     */
    suspend operator fun invoke(taskId: Long, localDateTime: LocalDateTime)
}
