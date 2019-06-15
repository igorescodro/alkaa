package com.escodro.domain.usecase.task

import com.escodro.core.extension.applySchedulers
import io.reactivex.Completable

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
    operator fun invoke(taskId: Long): Completable =
        getTask(taskId).flatMapCompletable {
            it.completed = true
            updateTask(it)
        }.applySchedulers()
}
