package com.escodro.domain.usecase.alarm

import com.escodro.domain.interactor.NotificationInteractor
import com.escodro.domain.provider.DateTimeProvider
import kotlinx.datetime.Instant
import mu.KotlinLogging
import kotlin.time.Duration.Companion.minutes

/**
 * Use case to snooze a task from the database.
 */
class SnoozeAlarm(
    private val dateTimeProvider: DateTimeProvider,
    private val notificationInteractor: NotificationInteractor,
    // private val alarmInteractor: AlarmInteractor, TODO
) {

    private val logger = KotlinLogging.logger {}

    /**
     * Snoozes the task.
     *
     * @param taskId the task id
     * @param minutes time to be snoozed in minutes
     *
     * @return observable to be subscribe
     */
    operator fun invoke(taskId: Long, minutes: Int = DEFAULT_SNOOZE) {
        require(minutes > 0) { "The delay minutes must be positive" }

        val snoozedTime = getSnoozedTask(dateTimeProvider.getCurrentInstant(), minutes)
        // alarmInteractor.schedule(taskId, snoozedTime)
        notificationInteractor.dismiss(taskId)
        logger.debug { "Task snoozed in $minutes minutes" }
    }

    private fun getSnoozedTask(instant: Instant, minutes: Int): Long {
        val updatedCalendar = instant.plus(minutes.minutes)
        return updatedCalendar.toEpochMilliseconds()
    }

    companion object {

        private const val DEFAULT_SNOOZE = 15
    }
}
