package com.escodro.domain.usecase.alarm

import com.escodro.domain.model.AlarmInterval
import com.escodro.domain.repository.TaskRepository
import timber.log.Timber

/**
 * Updates the task as repeating.
 */
class UpdateTaskAsRepeating(private val taskRepository: TaskRepository) {

    /**
     * Updates the task as repeating.
     *
     * @param taskId task id to be updated
     * @param interval repeating alarm interval
     */
    suspend operator fun invoke(taskId: Long, interval: AlarmInterval?) {
        val task = taskRepository.findTaskById(taskId)
        Timber.d("UpdateTaskAsRepeating = Task = '${task.title} as '$interval")

        val updatedTask = if (interval == null) {
            task.copy(alarmInterval = null, isRepeating = false)
        } else {
            task.copy(alarmInterval = interval, isRepeating = true)
        }

        taskRepository.updateTask(updatedTask)
    }
}
