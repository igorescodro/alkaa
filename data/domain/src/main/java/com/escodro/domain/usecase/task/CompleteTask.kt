package com.escodro.domain.usecase.task

import com.escodro.domain.calendar.TaskCalendar
import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository

/**
 * Use case to set a task as completed in the database.
 */
class CompleteTask(
    private val taskRepository: TaskRepository,
    private val taskCalendar: TaskCalendar
) {

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
    }

    /**
     * Completes the given task.
     *
     * @param taskId the task id
     *
     * @return observable to be subscribe
     */
    suspend operator fun invoke(taskId: Long) {
        val task = taskRepository.findTaskById(taskId)
        val completedTask = updateTaskAsCompleted(task)
        taskRepository.updateTask(completedTask)
    }

    private fun updateTaskAsCompleted(task: Task) =
        task.copy(completed = true, completedDate = taskCalendar.getCurrentCalendar())
}
