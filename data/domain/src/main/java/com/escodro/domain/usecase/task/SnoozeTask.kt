package com.escodro.domain.usecase.task

import com.escodro.domain.interactor.NotificationInteractor
import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import java.util.Calendar
import timber.log.Timber

/**
 * Use case to snooze a task from the database.
 */
class SnoozeTask(
    private val taskRepository: TaskRepository,
    private val notificationInteractor: NotificationInteractor
) {

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
        Timber.d("Task snoozed in $minutes minutes")

        val task = taskRepository.findTaskById(taskId)
        val snoozedTask = getSnoozedTask(task, minutes)
        taskRepository.updateTask(snoozedTask)
        notificationInteractor.dismiss(task.id)
    }

    private fun getSnoozedTask(task: Task, minutes: Int): Task {
        val updatedCalendar = task.dueDate?.apply { add(Calendar.MINUTE, minutes) }
        return task.copy(dueDate = updatedCalendar)
    }

    companion object {

        private const val DEFAULT_SNOOZE = 15
    }
}
