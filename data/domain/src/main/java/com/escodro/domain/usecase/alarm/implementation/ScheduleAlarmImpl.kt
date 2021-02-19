package com.escodro.domain.usecase.alarm.implementation

import com.escodro.domain.interactor.AlarmInteractor
import com.escodro.domain.repository.TaskRepository
import com.escodro.domain.usecase.alarm.ScheduleAlarm
import java.util.Calendar

internal class ScheduleAlarmImpl(
    private val taskRepository: TaskRepository,
    private val alarmInteractor: AlarmInteractor
) : ScheduleAlarm {

    /**
     * Schedules a new alarm.
     *
     * @param taskId the alarm id
     * @param calendar the time to the alarm be scheduled
     */
    override suspend operator fun invoke(taskId: Long, calendar: Calendar) {
        val task = taskRepository.findTaskById(taskId) ?: return
        val updatedTask = task.copy(dueDate = calendar)
        taskRepository.updateTask(updatedTask)

        alarmInteractor.schedule(taskId, calendar.time.time)
    }
}
