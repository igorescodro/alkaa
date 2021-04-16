package com.escodro.task.presentation.fake

import com.escodro.domain.usecase.alarm.CancelAlarm

internal class CancelAlarmFake : CancelAlarm {

    private val updatedList = mutableListOf<Long>()

    override suspend fun invoke(taskId: Long) {
        updatedList.add(taskId)
    }

    fun isAlarmCancelled(taskId: Long): Boolean =
        updatedList.contains(taskId)
}
