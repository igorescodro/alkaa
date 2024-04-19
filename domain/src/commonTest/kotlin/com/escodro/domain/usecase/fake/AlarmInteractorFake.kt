package com.escodro.domain.usecase.fake

import com.escodro.domain.interactor.AlarmInteractor
import com.escodro.domain.model.Task

internal class AlarmInteractorFake : AlarmInteractor {

    private val alarmMap: MutableMap<Long, Long> = mutableMapOf()

    var updatedTask: Task? = null

    override fun schedule(task: Task, timeInMillis: Long) {
        alarmMap[task.id] = timeInMillis
    }

    override fun cancel(task: Task) {
        alarmMap.remove(task.id)
    }

    override fun update(task: Task) {
        updatedTask = task
    }

    fun isAlarmScheduled(alarmId: Long): Boolean =
        alarmMap.contains(alarmId)

    fun getAlarmTime(alarmId: Long): Long? =
        alarmMap[alarmId]

    fun clear() {
        alarmMap.clear()
        updatedTask = null
    }
}
