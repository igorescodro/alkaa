package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task
import kotlinx.coroutines.flow.Flow

/**
 * Use case to get a task from the database as a Flow.
 */
interface LoadTaskFlow {
    /**
     * Gets a task as a Flow.
     *
     * @param taskId the task id
     *
     * @return a Flow of the selected task
     */
    operator fun invoke(taskId: Long): Flow<Task?>
}
