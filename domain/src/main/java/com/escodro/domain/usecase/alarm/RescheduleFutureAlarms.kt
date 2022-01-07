package com.escodro.domain.usecase.alarm

import com.escodro.domain.interactor.AlarmInteractor
import com.escodro.domain.model.Task
import com.escodro.domain.provider.CalendarProvider
import com.escodro.domain.repository.TaskRepository
import mu.KLogging
import java.util.Calendar

/**
 * Use case to reschedule tasks scheduled in the future or missing repeating.
 */
class RescheduleFutureAlarms(
    private val taskRepository: TaskRepository,
    private val alarmInteractor: AlarmInteractor,
    private val calendarProvider: CalendarProvider,
    private val scheduleNextAlarm: ScheduleNextAlarm
) {

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

    private fun isInFuture(calendar: Calendar?): Boolean {
        val currentTime = calendarProvider.getCurrentCalendar()
        return calendar?.after(currentTime) ?: false
    }

    private fun isMissedRepeating(task: Task): Boolean {
        val currentTime = calendarProvider.getCurrentCalendar()
        return task.isRepeating && task.dueDate?.before(currentTime) ?: false
    }

    private fun rescheduleFutureTask(task: Task) {
        val futureTime = task.dueDate?.time?.time ?: return
        alarmInteractor.schedule(task.id, futureTime)
        logger.debug { "Task '${task.title} rescheduled to '${task.dueDate}" }
    }

    private suspend fun rescheduleRepeatingTask(task: Task) {
        scheduleNextAlarm(task)
        logger.debug { "Repeating task '${task.title} rescheduled to '${task.dueDate}" }
    }

    companion object : KLogging()
}
