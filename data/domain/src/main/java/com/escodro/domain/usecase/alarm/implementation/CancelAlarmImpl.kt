package com.escodro.domain.usecase.alarm.implementation

import com.escodro.domain.interactor.AlarmInteractor
import com.escodro.domain.repository.TaskRepository
import com.escodro.domain.usecase.alarm.CancelAlarm

internal class CancelAlarmImpl(
    private val taskRepository: TaskRepository,
    private val alarmInteractor: AlarmInteractor
) : CancelAlarm {

    override suspend operator fun invoke(taskId: Long) {
        val task = taskRepository.findTaskById(taskId) ?: return

        val updatedTask = task.copy(dueDate = null)

        alarmInteractor.cancel(task.id)
        taskRepository.updateTask(updatedTask)
    }
}
