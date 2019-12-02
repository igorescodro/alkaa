package com.escodro.domain.usecase.task

import com.escodro.core.extension.applySchedulers
import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import io.reactivex.Completable

/**
 * Use case to update a task from the database.
 */
class UpdateTask(private val taskRepository: TaskRepository) {

    /**
     * Updates a task.
     *
     * @param task the task to be updated
     *
     * @return observable to be subscribe
     */
    operator fun invoke(task: Task): Completable =
        taskRepository.updateTask(task).applySchedulers()
}
