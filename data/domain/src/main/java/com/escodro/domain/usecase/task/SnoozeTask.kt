package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import java.util.Calendar

/**
 * Use case to snooze a task from the database.
 */
class SnoozeTask(private val taskRepository: TaskRepository) {

    /**
     * Snoozes the task.
     *
     * @param taskId the task id
     * @param minutes time to be snoozed in minutes
     *
     * @return observable to be subscribe
     */
    suspend operator fun invoke(taskId: Long, minutes: Int) {
        require(minutes > 0) { "The delay minutes must be positive" }

        val task = taskRepository.findTaskById(taskId)
        val snoozedTask = getSnoozedTask(task, minutes)
        taskRepository.updateTask(snoozedTask)
    }

    private fun getSnoozedTask(task: Task, minutes: Int): Task {
        val updatedCalendar = task.dueDate?.apply { add(Calendar.MINUTE, minutes) }
        return task.copy(dueDate = updatedCalendar)
    }
}
