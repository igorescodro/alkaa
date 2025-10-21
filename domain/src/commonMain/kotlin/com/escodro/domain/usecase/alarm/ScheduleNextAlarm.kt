package com.escodro.domain.usecase.alarm

import com.escodro.domain.interactor.AlarmInteractor
import com.escodro.domain.model.AlarmInterval
import com.escodro.domain.model.AlarmInterval.DAILY
import com.escodro.domain.model.AlarmInterval.HOURLY
import com.escodro.domain.model.AlarmInterval.MONTHLY
import com.escodro.domain.model.AlarmInterval.WEEKLY
import com.escodro.domain.model.AlarmInterval.YEARLY
import com.escodro.domain.model.Task
import com.escodro.domain.provider.DateTimeProvider
import com.escodro.domain.repository.TaskRepository
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import mu.KotlinLogging
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Schedules the next alarm entry or the missing ones in a repeating alarm.
 */
@OptIn(ExperimentalTime::class)
class ScheduleNextAlarm(
    private val taskRepository: TaskRepository,
    private val alarmInteractor: AlarmInteractor,
    private val dateTimeProvider: DateTimeProvider,
) {

    private val logger = KotlinLogging.logger {}

    /**
     * Schedules the next alarm.
     *
     * @param task task to be rescheduled
     */
    suspend operator fun invoke(task: Task) {
        require(task.isRepeating) { "Task is not repeating" }
        require(task.dueDate != null) { "Task has no due date" }
        require(task.alarmInterval != null) { "Task has no alarm interval" }

        val currentTime = dateTimeProvider.getCurrentInstant()
        var taskTime = task.dueDate.toInstant(TimeZone.currentSystemDefault())
        do {
            taskTime = updatedAlarmTime(taskTime, task.alarmInterval)
        } while (currentTime > taskTime)

        val updatedTask =
            task.copy(dueDate = taskTime.toLocalDateTime(TimeZone.currentSystemDefault()))

        taskRepository.updateTask(updatedTask)
        alarmInteractor.schedule(updatedTask, taskTime.toEpochMilliseconds())
        logger.debug { "ScheduleNextAlarm = Task = '${task.title}' at $taskTime " }
    }

    private fun updatedAlarmTime(instant: Instant, alarmInterval: AlarmInterval): Instant {
        val timeZone = TimeZone.currentSystemDefault()
        val period = when (alarmInterval) {
            HOURLY -> DateTimePeriod(hours = 1)
            DAILY -> DateTimePeriod(days = 1)
            WEEKLY -> DateTimePeriod(days = 7)
            MONTHLY -> DateTimePeriod(months = 1)
            YEARLY -> DateTimePeriod(years = 1)
        }
        return instant.plus(period, timeZone)
    }
}
