package com.escodro.domain.usecase.alarm

import com.escodro.domain.interactor.AlarmInteractor
import com.escodro.domain.model.Task
import com.escodro.domain.provider.DateTimeProvider
import com.escodro.domain.repository.TaskRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import mu.KotlinLogging

/**
 * Use case to reschedule tasks scheduled in the future or missing repeating.
 */
class RescheduleFutureAlarms(
    private val taskRepository: TaskRepository,
    private val alarmInteractor: AlarmInteractor?, // TODO not null
    private val dateTimeProvider: DateTimeProvider,
    private val scheduleNextAlarm: ScheduleNextAlarm,
) {

    private val logger = KotlinLogging.logger {}

    /**
     * Reschedule scheduled and misses repeating tasks.
     */
    suspend operator fun invoke() {
        val uncompletedAlarms = taskRepository.findAllTasksWithDueDate().filterNot { it.completed }
        val futureAlarms = uncompletedAlarms.filter { isInFuture(it.dueDate) }
        val missedRepeating = uncompletedAlarms.filter { isMissedRepeating(it) }

        futureAlarms.forEach { rescheduleFutureTask(it) }
        missedRepeating.forEach { rescheduleRepeatingTask(it) }
    }

    private fun isInFuture(localDateTime: LocalDateTime?): Boolean {
        val currentTime = dateTimeProvider.getCurrentInstant()
        val givenTime = localDateTime?.toInstant(TimeZone.currentSystemDefault()) ?: return false
        return givenTime > currentTime
    }

    private fun isMissedRepeating(task: Task): Boolean {
        val currentTime = Clock.System.now()
        val givenTime = task.dueDate?.toInstant(TimeZone.currentSystemDefault()) ?: return false
        return task.isRepeating && currentTime > givenTime
    }

    private fun rescheduleFutureTask(task: Task) {
        val futureTime = task.dueDate
            ?.toInstant(TimeZone.currentSystemDefault())?.toEpochMilliseconds() ?: return
        alarmInteractor?.schedule(task, futureTime)
        logger.debug { "Task '${task.title} rescheduled to '${task.dueDate}" }
    }

    private suspend fun rescheduleRepeatingTask(task: Task) {
        scheduleNextAlarm(task)
        logger.debug { "Repeating task '${task.title} rescheduled to '${task.dueDate}" }
    }
}
