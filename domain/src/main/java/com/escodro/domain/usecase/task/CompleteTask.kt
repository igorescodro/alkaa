package com.escodro.domain.usecase.task

import io.reactivex.Single

/**
 * Use case to set a task as completed in the database.
 */
class CompleteTask(private val getTask: GetTask, private val updateTask: UpdateTask) {

    /**
     * Completes the given task.
     *
     * @param taskId the task id
     *
     * @return observable to be subscribe
     */
    operator fun invoke(taskId: Long): Single<Unit> =
        getTask(taskId).map {
            it.completed = true
            updateTask(it)
        }
}
