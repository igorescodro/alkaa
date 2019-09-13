package com.escodro.domain.usecase.task

import com.escodro.core.extension.applySchedulers
import io.reactivex.Completable

/**
 * Use case to set a task as uncompleted in the database.
 */
class UncompleteTask(private val getTask: GetTask, private val updateTask: UpdateTask) {

    /**
     * Completes the given task.
     *
     * @param taskId the task id
     *
     * @return observable to be subscribe
     */
    operator fun invoke(taskId: Long): Completable =
        getTask(taskId).flatMapCompletable {
            it.completed = false
            updateTask(it)
        }.applySchedulers()
}
