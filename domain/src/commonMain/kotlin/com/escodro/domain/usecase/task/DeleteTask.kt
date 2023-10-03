package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import mu.KotlinLogging

/**
 * Use case to delete a task from the database.
 */
class DeleteTask(
    private val taskRepository: TaskRepository,
    // private val alarmInteractor: AlarmInteractor, TODO
) {

    private val logger = KotlinLogging.logger {}

    /**
     * Deletes a task.
     *
     * @param task the task to be deleted
     *
     * @return observable to be subscribe
     */
    suspend operator fun invoke(task: Task) {
        logger.debug { "Deleting task ${task.title}" }
        taskRepository.deleteTask(task)
        // alarmInteractor.cancel(task.id)
    }
}
