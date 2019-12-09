package com.escodro.domain.usecase.task

import com.escodro.core.extension.applySchedulers
import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import io.reactivex.Completable

/**
 * Use case to delete a task from the database.
 */
class DeleteTask(private val taskRepository: TaskRepository) {

    /**
     * Deletes a task.
     *
     * @param task the task to be deleted
     *
     * @return observable to be subscribe
     */
    operator fun invoke(task: Task): Completable =
        taskRepository.deleteTask(task).applySchedulers()
}
