package com.escodro.domain.usecase.fake

import com.escodro.domain.model.Task
import com.escodro.domain.usecase.alarm.UpdateAlarm

internal class UpdateAlarmFake : UpdateAlarm {

    private val updatedMap = HashMap<Long, Task>()

    override suspend fun invoke(task: Task) {
        updatedMap[task.id] = task
    }
}
