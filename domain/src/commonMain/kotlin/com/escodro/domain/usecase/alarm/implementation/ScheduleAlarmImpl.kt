package com.escodro.domain.usecase.alarm.implementation

import com.escodro.domain.interactor.AlarmInteractor
import com.escodro.domain.repository.TaskRepository
import com.escodro.domain.usecase.alarm.ScheduleAlarm
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
internal class ScheduleAlarmImpl(
    private val taskRepository: TaskRepository,
    private val alarmInteractor: AlarmInteractor,
) : ScheduleAlarm {

    /**
     * Schedules a new alarm.
     *
     * @param taskId the alarm id
     * @param localDateTime the time to the alarm be scheduled
     */
    override suspend operator fun invoke(taskId: Long, localDateTime: LocalDateTime) {
        val task = taskRepository.findTaskById(taskId) ?: return
        val updatedTask = task.copy(dueDate = localDateTime)
        taskRepository.updateTask(updatedTask)

        alarmInteractor.schedule(
            task,
            localDateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds(),
        )
    }
}
