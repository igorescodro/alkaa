package com.escodro.domain.usecase.alarm

import com.escodro.domain.interactor.AlarmInteractor
import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import java.util.Calendar
import timber.log.Timber

/**
 * Use case to get all tasks scheduled in the future that are not completed from the database.
 */
class RescheduleFutureAlarms(
    private val taskRepository: TaskRepository,
    private val alarmInteractor: AlarmInteractor
) {

    /**
     * Gets all the uncompleted tasks in the future.
     *
     * @return observable to be subscribe
     */
    suspend operator fun invoke() =
        taskRepository.findAllTasksWithDueDate()
            .filter { !it.completed }
            .filter { isInFuture(it.dueDate) }
            .forEach { rescheduleTask(it) }

    private fun isInFuture(calendar: Calendar?): Boolean {
        val currentTime = Calendar.getInstance()
        return calendar?.after(currentTime) ?: false
    }

    private fun rescheduleTask(task: Task) {
        val futureTime = task.dueDate?.time?.time ?: return
        alarmInteractor.schedule(task.id, futureTime)
        Timber.d("Task '${task.title} rescheduled to '${task.dueDate}")
    }
}
