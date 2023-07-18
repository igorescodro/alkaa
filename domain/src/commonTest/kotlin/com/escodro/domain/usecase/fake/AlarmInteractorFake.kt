package com.escodro.domain.usecase.fake

import com.escodro.domain.interactor.AlarmInteractor

internal class AlarmInteractorFake : AlarmInteractor {

    private val alarmMap: MutableMap<Long, Long> = mutableMapOf()

    override fun schedule(alarmId: Long, timeInMillis: Long) {
        alarmMap[alarmId] = timeInMillis
    }

    override fun cancel(alarmId: Long) {
        alarmMap.remove(alarmId)
    }

    fun isAlarmScheduled(alarmId: Long): Boolean =
        alarmMap.contains(alarmId)

    fun getAlarmTime(alarmId: Long): Long? =
        alarmMap[alarmId]

    fun clear() {
        alarmMap.clear()
    }
}
