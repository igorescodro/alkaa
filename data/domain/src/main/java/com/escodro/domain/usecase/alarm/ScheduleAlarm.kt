package com.escodro.domain.usecase.alarm

import com.escodro.domain.interactor.AlarmInteractor
import com.escodro.domain.repository.TaskRepository
import java.util.Calendar

/**
 * Use case to schedule a new alarm.
 */
class ScheduleAlarm(
    private val taskRepository: TaskRepository,
    private val alarmInteractor: AlarmInteractor
) {

    /**
     * Schedules a new alarm.
     *
     * @param taskId the alarm id
     * @param calendar the time to the alarm be scheduled
     */
    suspend operator fun invoke(taskId: Long, calendar: Calendar) {
        val task = taskRepository.findTaskById(taskId) ?: return
        val updatedTask = task.copy(dueDate = calendar)
        taskRepository.updateTask(updatedTask)

        alarmInteractor.schedule(taskId, calendar.time.time)
    }
}
