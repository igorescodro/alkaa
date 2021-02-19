package com.escodro.task.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escodro.domain.usecase.alarm.CancelAlarm
import com.escodro.domain.usecase.alarm.ScheduleAlarm
import com.escodro.domain.usecase.alarm.UpdateTaskAsRepeating
import com.escodro.task.mapper.AlarmIntervalMapper
import com.escodro.task.model.AlarmInterval
import kotlinx.coroutines.launch
import java.util.Calendar

internal class TaskAlarmViewModel(
    private val scheduleAlarmUseCase: ScheduleAlarm,
    private val updateTaskAsRepeatingUseCase: UpdateTaskAsRepeating,
    private val cancelAlarmUseCase: CancelAlarm,
    private val alarmIntervalMapper: AlarmIntervalMapper
) : ViewModel() {

    fun setAlarm(taskId: Long, alarm: Calendar) = viewModelScope.launch {
        scheduleAlarmUseCase(taskId, alarm)
    }

    fun setRepeating(taskId: Long, alarmInterval: AlarmInterval) = viewModelScope.launch {
        val interval = alarmIntervalMapper.toDomain(alarmInterval)
        updateTaskAsRepeatingUseCase(taskId, interval)
    }

    fun removeAlarm(taskId: Long) = viewModelScope.launch {
        cancelAlarmUseCase(taskId)
    }
}
