package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import io.reactivex.Completable
import java.util.Calendar

/**
 * Use case to snooze a task from the database.
 */
class SnoozeTask(private val taskRepository: TaskRepository) {

    /**
     * Snoozes the task.
     *
     * @param taskId the task id
     * @param minutes time to be snoozed in minutes
     *
     * @return observable to be subscribe
     */
    operator fun invoke(taskId: Long, minutes: Int): Completable {
        if (minutes <= 0) {
            return Completable.error(IllegalArgumentException("The delay minutes must be positive"))
        }

        return taskRepository.findTaskById(taskId)
            .map { task -> getSnoozedTask(task, minutes) }
            .flatMapCompletable { task -> taskRepository.updateTask(task) }
    }

    private fun getSnoozedTask(task: Task, minutes: Int): Task {
        val updatedCalendar = task.dueDate?.apply { add(Calendar.MINUTE, minutes) }
        return task.copy(dueDate = updatedCalendar)
    }
}
