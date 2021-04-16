package com.escodro.task.presentation.fake

import com.escodro.domain.model.AlarmInterval
import com.escodro.domain.usecase.alarm.UpdateTaskAsRepeating

internal class UpdateTaskAsRepeatingFake : UpdateTaskAsRepeating {

    private val updatedMap = HashMap<Long, AlarmInterval?>()

    override suspend fun invoke(taskId: Long, interval: AlarmInterval?) {
        updatedMap[taskId] = interval
    }

    fun isAlarmUpdated(taskId: Long): Boolean =
        updatedMap.containsKey(taskId)

    fun getUpdatedAlarm(taskId: Long): AlarmInterval? =
        updatedMap[taskId]
}
