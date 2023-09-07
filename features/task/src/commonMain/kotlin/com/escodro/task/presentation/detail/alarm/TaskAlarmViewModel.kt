package com.escodro.task.presentation.detail.alarm

import com.escodro.coroutines.AppCoroutineScope
import com.escodro.domain.usecase.alarm.CancelAlarm
import com.escodro.domain.usecase.alarm.ScheduleAlarm
import com.escodro.domain.usecase.alarm.UpdateTaskAsRepeating
import com.escodro.task.mapper.AlarmIntervalMapper
import com.escodro.task.model.AlarmInterval
import com.escodro.task.presentation.detail.main.TaskId
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.datetime.LocalDateTime

internal class TaskAlarmViewModel(
    private val scheduleAlarmUseCase: ScheduleAlarm,
    private val updateTaskAsRepeatingUseCase: UpdateTaskAsRepeating,
    private val cancelAlarmUseCase: CancelAlarm,
    private val applicationScope: AppCoroutineScope,
    private val alarmIntervalMapper: AlarmIntervalMapper,
) : ViewModel() {

    fun updateAlarm(taskId: TaskId, alarm: LocalDateTime?) = applicationScope.launch {
        if (alarm != null) {
            scheduleAlarmUseCase(taskId.value, alarm)
        } else {
            cancelAlarmUseCase(taskId.value)
        }
    }

    fun setRepeating(taskId: TaskId, alarmInterval: AlarmInterval) = applicationScope.launch {
        val interval = alarmIntervalMapper.toDomain(alarmInterval)
        updateTaskAsRepeatingUseCase(taskId.value, interval)
    }
}
