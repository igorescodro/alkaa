package com.escodro.task.presentation.detail.alarm

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.escodro.alarm.notification.TaskNotificationScheduler
import com.escodro.task.presentation.detail.TaskDetailProvider
import timber.log.Timber
import java.util.Calendar

/**
 * [ViewModel] responsible to provide information to Task Alarm layout.
 */
internal class TaskAlarmViewModel(
    private val alarmManager: TaskNotificationScheduler,
    private val taskProvider: TaskDetailProvider
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
            taskProvider.updateTask(it)
            alarmManager.scheduleTaskAlarm(it.id, it.dueDate?.time?.time)
        }
    }

    /**
     * Removes the task alarm.
     */
    fun removeAlarm() {
        Timber.d("removeAlarm()")

        taskData.value?.copy(dueDate = null)?.let {
            alarmManager.cancelTaskAlarm(it.id)
            taskProvider.updateTask(it)
        }
    }
}
