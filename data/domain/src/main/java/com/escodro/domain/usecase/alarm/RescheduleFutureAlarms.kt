package com.escodro.domain.usecase.alarm

import com.escodro.domain.interactor.AlarmInteractor
import com.escodro.domain.model.Task
import com.escodro.domain.provider.CalendarProvider
import com.escodro.domain.repository.TaskRepository
import java.util.Calendar
import timber.log.Timber

/**
 * Use case to reschedule tasks scheduled in the future or missing repeating.
 */
class RescheduleFutureAlarms(
    private val taskRepository: TaskRepository,
    private val alarmInteractor: AlarmInteractor,
    private val calendarProvider: CalendarProvider
) {

    /**
     * Gets all the uncompleted tasks in the future.
     *
     * @return observable to be subscribe
     */
    suspend operator fun invoke() =
        taskRepository.findAllTasksWithDueDate()
            .filterNot { it.completed }
            .filter { isInFuture(it.dueDate) || isMissedRepeating(it) }
            .forEach { rescheduleTask(it) }

    private fun isInFuture(calendar: Calendar?): Boolean {
        val currentTime = calendarProvider.getCurrentCalendar()
        return calendar?.after(currentTime) ?: false
    }

    private fun isMissedRepeating(task: Task): Boolean {
        val currentTime = calendarProvider.getCurrentCalendar()
        return task.isRepeating && task.dueDate?.before(currentTime) ?: false
    }

    private fun rescheduleTask(task: Task) {
        val futureTime = task.dueDate?.time?.time ?: return
        alarmInteractor.schedule(task.id, futureTime)
        Timber.d("Task '${task.title} rescheduled to '${task.dueDate}")
    }
}
