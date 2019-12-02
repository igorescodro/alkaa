package com.escodro.domain.usecase.task

import com.escodro.core.extension.applySchedulers
import com.escodro.domain.repository.TaskRepository
import io.reactivex.Completable

/**
 * Use case to update a task as completed or uncompleted from the database.
 */
class UpdateTaskStatus(
    private val taskRepository: TaskRepository,
    private val completeTask: CompleteTask,
    private val uncompleteTask: UncompleteTask
) {

    /**
     * Updates the task as completed or uncompleted based on the current state.
     *
     * @param taskId the id from the task to be updated
     *
     * @return observable to be subscribe
     */
    operator fun invoke(taskId: Long): Completable =
        taskRepository.findTaskById(taskId)
            .flatMapCompletable { task ->
                when (!task.completed) {
                    true -> completeTask(task)
                    false -> uncompleteTask(task)
                }
            }.applySchedulers()
}
