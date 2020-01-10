package com.escodro.task.presentation.detail.alarm

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escodro.domain.usecase.alarm.CancelAlarm
import com.escodro.domain.usecase.alarm.ScheduleAlarm
import com.escodro.task.presentation.detail.TaskDetailProvider
import java.util.Calendar
import timber.log.Timber

/**
 * [ViewModel] responsible to provide information to Task Alarm layout.
 */
internal class TaskAlarmViewModel(
    private val taskProvider: TaskDetailProvider,
    private val scheduleAlarmUseCase: ScheduleAlarm,
    private val cancelAlarmUseCase: CancelAlarm
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

        taskData.value?.copy(dueDate = alarm)?.let {
            taskProvider.updateTask(it, viewModelScope)
            scheduleAlarmUseCase(it.id, it.dueDate?.time?.time)
        }
    }

    /**
     * Removes the task alarm.
     */
    fun removeAlarm() {
        Timber.d("removeAlarm()")

        taskData.value?.copy(dueDate = null)?.let {
            cancelAlarmUseCase(it.id)
            taskProvider.updateTask(it, viewModelScope)
        }
    }
}
