package com.escodro.domain.usecase.alarm

import com.escodro.domain.interactor.NotificationInteractor
import com.escodro.domain.repository.TaskRepository
import timber.log.Timber

/**
 * Use case to show an alarm.
 */
class ShowAlarm(
    private val taskRepository: TaskRepository,
    private val notificationInteractor: NotificationInteractor,
    private val scheduleNextAlarm: ScheduleNextAlarm
) {

    /**
     * Shows the alarm.
     *
     * @param taskId the alarm id to be shown
     */
    suspend operator fun invoke(taskId: Long) {
        val task = taskRepository.findTaskById(taskId)

        if (task.completed) {
            Timber.d("Task '${task.title}' is already completed. Will not notify")
            return
        } else {
            Timber.d("Notifying task '${task.title}'")
            notificationInteractor.show(task)
        }

        if (task.isRepeating) {
            scheduleNextAlarm(task)
        }
    }
}
