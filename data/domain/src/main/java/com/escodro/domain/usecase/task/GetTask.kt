package com.escodro.domain.usecase.task

import com.escodro.core.extension.applySchedulers
import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import io.reactivex.Single

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
    operator fun invoke(taskId: Long): Single<Task> =
        taskRepository.findTaskById(taskId).applySchedulers()
}
