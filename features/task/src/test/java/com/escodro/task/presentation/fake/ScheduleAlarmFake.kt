package com.escodro.task.presentation.fake

import com.escodro.domain.usecase.alarm.ScheduleAlarm
import java.util.Calendar

internal class ScheduleAlarmFake : ScheduleAlarm {

    private val updatedMap = HashMap<Long, Calendar>()

    override suspend fun invoke(taskId: Long, calendar: Calendar) {
        updatedMap[taskId] = calendar
    }

    fun isAlarmScheduled(taskId: Long): Boolean =
        updatedMap.containsKey(taskId)

    fun getScheduledAlarm(taskId: Long): Calendar? =
        updatedMap[taskId]
}
