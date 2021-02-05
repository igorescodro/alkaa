package com.escodro.domain.usecase.alarm

import com.escodro.domain.interactor.AlarmInteractor
import com.escodro.domain.repository.TaskRepository

/**
 * Use case to cancel an alarm.
 */
class CancelAlarm(
    private val taskRepository: TaskRepository,
    private val alarmInteractor: AlarmInteractor
) {

    /**
     * Cancels an alarm.
     *
     * @param taskId the task id
     */
    suspend operator fun invoke(taskId: Long) {
        val task = taskRepository.findTaskById(taskId) ?: return

        val updatedTask = task.copy(dueDate = null)

        alarmInteractor.cancel(task.id)
        taskRepository.updateTask(updatedTask)
    }
}
