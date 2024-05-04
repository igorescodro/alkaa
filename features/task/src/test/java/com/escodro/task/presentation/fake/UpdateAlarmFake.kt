package com.escodro.task.presentation.fake

import com.escodro.domain.model.Task
import com.escodro.domain.usecase.alarm.UpdateAlarm

internal class UpdateAlarmFake : UpdateAlarm {

    private val updatedMap = HashMap<Long, Task>()

    fun getScheduledTask(taskId: Long): Task? =
        updatedMap[taskId]

    override suspend fun invoke(task: Task) {
        updatedMap[task.id] = task
    }
}
