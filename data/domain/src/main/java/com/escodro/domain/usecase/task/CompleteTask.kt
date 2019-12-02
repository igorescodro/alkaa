package com.escodro.domain.usecase.task

import com.escodro.core.extension.applySchedulers
import com.escodro.domain.calendar.TaskCalendar
import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import io.reactivex.Completable

/**
 * Use case to set a task as completed in the database.
 */
class CompleteTask(
    private val taskRepository: TaskRepository,
    private val taskCalendar: TaskCalendar
) {

    /**
     * Completes the given task.
     *
     * @param task the task to be updated
     *
     * @return observable to be subscribe
     */
    operator fun invoke(task: Task): Completable {
        val updatedTask = updateTaskAsCompleted(task)
        return taskRepository.updateTask(updatedTask).applySchedulers()
    }

    /**
     * Completes the given task.
     *
     * @param taskId the task id
     *
     * @return observable to be subscribe
     */
    operator fun invoke(taskId: Long): Completable =
        taskRepository.findTaskById(taskId)
            .map { task -> updateTaskAsCompleted(task) }
            .flatMapCompletable { task -> taskRepository.updateTask(task) }
            .applySchedulers()

    private fun updateTaskAsCompleted(task: Task) =
        task.copy(completed = true, completedDate = taskCalendar.getCurrentCalendar())
}
