package com.escodro.domain.usecase.alarm.implementation

import com.escodro.domain.model.Task
import com.escodro.domain.usecase.alarm.CancelAlarm
import com.escodro.domain.usecase.alarm.ScheduleAlarm
import com.escodro.domain.usecase.alarm.UpdateAlarm
import com.escodro.domain.usecase.alarm.UpdateTaskAsRepeating

internal class UpdateAlarmImpl(
    private val scheduleAlarmUseCase: ScheduleAlarm,
    private val updateTaskAsRepeatingUseCase: UpdateTaskAsRepeating,
    private val cancelAlarmUseCase: CancelAlarm,
) : UpdateAlarm {

    override suspend fun invoke(task: Task) {
        if (task.dueDate != null) {
            scheduleAlarmUseCase(task.id, task.dueDate)
        } else {
            cancelAlarmUseCase(task.id)
        }

        if (task.alarmInterval != null) {
            updateTaskAsRepeatingUseCase(task.id, task.alarmInterval)
        }
    }
}
