package com.escodro.task.presentation.detail.alarm

import androidx.lifecycle.ViewModel
import com.escodro.coroutines.AppCoroutineScope
import com.escodro.domain.usecase.alarm.UpdateAlarm
import com.escodro.domain.usecase.task.LoadTask
import com.escodro.task.mapper.AlarmIntervalMapper
import com.escodro.task.model.AlarmInterval
import com.escodro.task.presentation.detail.main.TaskId
import kotlinx.datetime.LocalDateTime

internal class TaskAlarmViewModel(
    private val updateAlarm: UpdateAlarm,
    private val loadTask: LoadTask,
    private val applicationScope: AppCoroutineScope,
    private val alarmIntervalMapper: AlarmIntervalMapper,
) : ViewModel() {

    fun updateAlarm(taskId: TaskId, alarm: LocalDateTime?) = applicationScope.launch {
        val task = loadTask(taskId.value) ?: return@launch
        val updatedTask = task.copy(dueDate = alarm)
        updateAlarm(updatedTask)
    }

    fun setRepeating(taskId: TaskId, alarmInterval: AlarmInterval) = applicationScope.launch {
        val task = loadTask(taskId.value) ?: return@launch
        val interval = alarmIntervalMapper.toDomain(alarmInterval)
        val updatedTask = task.copy(alarmInterval = interval)
        updateAlarm(updatedTask)
    }
}
