package com.escodro.domain.usecase.task

import com.escodro.domain.interactor.AlarmInteractor
import com.escodro.domain.interactor.NotificationInteractor
import com.escodro.domain.model.Task
import com.escodro.domain.provider.CalendarProvider
import com.escodro.domain.repository.TaskRepository

/**
 * Use case to set a task as completed in the database.
 */
class CompleteTask(
    private val taskRepository: TaskRepository,
    private val alarmInteractor: AlarmInteractor,
    private val notificationInteractor: NotificationInteractor,
    private val calendarProvider: CalendarProvider
) {

    /**
     * Completes the given task.
     *
     * @param taskId the task id
     *
     * @return observable to be subscribe
     */
    suspend operator fun invoke(taskId: Long) {
        val task = taskRepository.findTaskById(taskId) ?: return
        invoke(task)
    }

    /**
     * Completes the given task.
     *
     * @param task the task to be updated
     *
     * @return observable to be subscribe
     */
    suspend operator fun invoke(task: Task) {
        val updatedTask = updateTaskAsCompleted(task)
        taskRepository.updateTask(updatedTask)
        alarmInteractor.cancel(task.id)
        notificationInteractor.dismiss(task.id)
    }

    private fun updateTaskAsCompleted(task: Task) =
        task.copy(completed = true, completedDate = calendarProvider.getCurrentCalendar())
}
