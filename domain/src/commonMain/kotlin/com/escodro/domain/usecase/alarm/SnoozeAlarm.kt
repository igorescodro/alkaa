package com.escodro.domain.usecase.alarm

import com.escodro.domain.interactor.AlarmInteractor
import com.escodro.domain.interactor.NotificationInteractor
import com.escodro.domain.provider.DateTimeProvider
import com.escodro.domain.usecase.task.LoadTask
import mu.KotlinLogging
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Use case to snooze a task from the database.
 */
@OptIn(ExperimentalTime::class)
class SnoozeAlarm(
    private val loadTask: LoadTask,
    private val dateTimeProvider: DateTimeProvider,
    private val notificationInteractor: NotificationInteractor,
    private val alarmInteractor: AlarmInteractor,
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
    suspend operator fun invoke(taskId: Long, minutes: Int = DEFAULT_SNOOZE) {
        require(minutes > 0) { "The delay minutes must be positive" }
        val task = loadTask(taskId = taskId) ?: return

        val snoozedTime = getSnoozedTask(dateTimeProvider.getCurrentInstant(), minutes)
        alarmInteractor.schedule(task, snoozedTime)
        notificationInteractor.dismiss(task)
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
