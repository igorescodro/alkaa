package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case to get a task from the database.
 */
class GetTask(private val taskRepository: TaskRepository) {

    /**
     * Gets a task.
     *
     * @param taskId the task id
     *
     * @return observable to be subscribe
     */
    operator fun invoke(taskId: Long): Flow<Task> =
        taskRepository.findTaskFlowById(taskId)
}
