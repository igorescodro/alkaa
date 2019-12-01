package com.escodro.domain.usecase.task

import com.escodro.core.extension.applySchedulers
import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import io.reactivex.Completable
import java.util.Calendar

/**
 * Use case to snooze a task from the database.
 */
class SnoozeTask(
    private val taskRepository: TaskRepository,
    private val getTask: GetTask,
    private val updateTask: UpdateTask
) {

    /**
     * Snoozes the task.
     *
     * @param taskId the task id
     * @param minutes time to be snoozed in minutes
     *
     * @return observable to be subscribe
     */
    operator fun invoke(taskId: Long, minutes: Int): Completable =
        getTask(taskId).flatMapCompletable {
            it.dueDate?.add(Calendar.MINUTE, minutes)
            updateTask(it)
        }.applySchedulers()

    @Suppress("UndocumentedPublicFunction")
    fun test(taskId: Long, minutes: Int): Completable =
        taskRepository.findTaskById(taskId)
            .map { task -> getSnoozedTask(task, minutes) }
            .flatMapCompletable { task -> taskRepository.updateTask(task) }

    private fun getSnoozedTask(task: Task, minutes: Int): Task {
        val updatedCalendar = task.dueDate?.apply { add(Calendar.MINUTE, minutes) }
        return task.copy(dueDate = updatedCalendar)
    }
}
