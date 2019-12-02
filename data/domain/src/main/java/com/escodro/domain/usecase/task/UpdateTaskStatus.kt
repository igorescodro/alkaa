package com.escodro.domain.usecase.task

import com.escodro.core.extension.applySchedulers
import com.escodro.domain.model.Task
import io.reactivex.Completable

/**
 * Use case to update a task as completed or uncompleted from the database.
 */
class UpdateTaskStatus(
    private val completeTask: CompleteTask,
    private val uncompleteTask: UncompleteTask
) {

    /**
     * Updates the task as completed or uncompleted based on the current state.
     *
     * @param task the task to be updated
     *
     * @return observable to be subscribe
     */
    operator fun invoke(task: Task): Completable {
        val isCompleted = task.completed

        return when (isCompleted) {
            true -> completeTask(task.id)
            false -> uncompleteTask(task.id)
        }.applySchedulers()
    }
}
