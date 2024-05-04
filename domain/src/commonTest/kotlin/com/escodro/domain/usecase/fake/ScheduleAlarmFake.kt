package com.escodro.domain.usecase.fake

import com.escodro.domain.usecase.alarm.ScheduleAlarm
import kotlinx.datetime.LocalDateTime

internal class ScheduleAlarmFake : ScheduleAlarm {

    private val updatedMap = HashMap<Long, LocalDateTime>()

    override suspend fun invoke(taskId: Long, localDateTime: LocalDateTime) {
        updatedMap[taskId] = localDateTime
    }

    fun isAlarmScheduled(taskId: Long): Boolean =
        updatedMap.containsKey(taskId)

    fun getScheduledAlarm(taskId: Long): LocalDateTime? =
        updatedMap[taskId]
}
