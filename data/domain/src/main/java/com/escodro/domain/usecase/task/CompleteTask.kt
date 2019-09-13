package com.escodro.domain.usecase.task

import com.escodro.core.extension.applySchedulers
import com.escodro.domain.calendar.TaskCalendar
import io.reactivex.Completable

/**
 * Use case to set a task as completed in the database.
 */
class CompleteTask(
    private val getTask: GetTask,
    private val updateTask: UpdateTask,
    private val taskCalendar: TaskCalendar
) {

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
            it.completedDate = taskCalendar.getCurrentCalendar()
            updateTask(it)
        }.applySchedulers()
}
