package com.escodro.task.presentation.detail.alarm

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escodro.domain.usecase.alarm.CancelAlarm
import com.escodro.domain.usecase.alarm.ScheduleAlarm
import com.escodro.domain.usecase.alarm.UpdateTaskAsRepeating
import com.escodro.task.mapper.AlarmIntervalMapper
import com.escodro.task.model.AlarmInterval
import com.escodro.task.presentation.detail.TaskDetailProvider
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Calendar

/**
 * [ViewModel] responsible to provide information to Task Alarm layout.
 */
internal class TaskAlarmViewModel(
    taskProvider: TaskDetailProvider,
    private val scheduleAlarmUseCase: ScheduleAlarm,
    private val cancelAlarmUseCase: CancelAlarm,
    private val scheduleRepeatingUseCase: UpdateTaskAsRepeating,
    private val alarmIntervalMapper: AlarmIntervalMapper
) : ViewModel() {

    val taskData = taskProvider.taskData

    val chipVisibility = MediatorLiveData<Boolean>()

    init {
        chipVisibility.addSource(taskData) { chipVisibility.value = it.dueDate != null }
    }

    /**
     * Sets an alarm to the task.
     *
     * @param alarm the date and time to ring the calendar
     */
    fun setAlarm(alarm: Calendar) {
        Timber.d("setAlarm()")

        viewModelScope.launch {
            taskData.value?.let { task ->
                scheduleAlarmUseCase(task.id, alarm)
            }
        }
    }

    fun setRepeating(alarmInterval: AlarmInterval) {
        Timber.d("setRepeating - ${alarmInterval.name}")

        viewModelScope.launch {
            taskData.value?.let { task ->
                val interval = alarmIntervalMapper.toDomain(alarmInterval)
                scheduleRepeatingUseCase(task.id, interval)
            }
        }
    }

    /**
     * Removes the task alarm.
     */
    fun removeAlarm() {
        Timber.d("removeAlarm()")

        viewModelScope.launch {
            taskData.value?.let { task ->
                cancelAlarmUseCase(task.id)
            }
        }
    }
}
