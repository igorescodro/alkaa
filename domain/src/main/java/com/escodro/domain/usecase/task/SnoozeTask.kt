package com.escodro.domain.usecase.task

import io.reactivex.Single
import java.util.Calendar

/**
 * Use case to snooze a task from the database.
 */
class SnoozeTask(private val getTask: GetTask, private val updateTask: UpdateTask) {

    /**
     * Snoozes the task.
     *
     * @param taskId the task id
     * @param minutes time to be snoozed in minutes
     *
     * @return observable to be subscribe
     */
    operator fun invoke(taskId: Long, minutes: Int): Single<Unit> =
        getTask(taskId).map {
            it.dueDate?.add(Calendar.MINUTE, minutes)
            updateTask(it)
        }
}
