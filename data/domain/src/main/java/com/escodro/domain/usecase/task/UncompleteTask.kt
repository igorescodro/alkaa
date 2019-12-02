package com.escodro.domain.usecase.task

import com.escodro.core.extension.applySchedulers
import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import io.reactivex.Completable

/**
 * Use case to set a task as uncompleted in the database.
 */
class UncompleteTask(private val taskRepository: TaskRepository) {

    /**
     * Completes the given task.
     *
     * @param taskId the task id
     *
     * @return observable to be subscribe
     */
    operator fun invoke(taskId: Long): Completable =
        taskRepository.findTaskById(taskId)
            .map { task -> updateTaskAsUncompleted(task) }
            .flatMapCompletable { task -> taskRepository.updateTask(task) }
            .applySchedulers()

    private fun updateTaskAsUncompleted(task: Task) =
        task.copy(completed = false, completedDate = null)
}
