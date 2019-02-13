package com.escodro.alkaa.ui.task.detail.alarm

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.escodro.alkaa.common.extension.notify
import com.escodro.alkaa.data.local.model.Task
import com.escodro.alkaa.ui.task.alarm.notification.TaskNotificationScheduler
import com.escodro.alkaa.ui.task.detail.TaskDetailProvider
import com.escodro.alkaa.ui.task.detail.main.TaskDetailContract
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.util.Calendar

class TaskAlarmViewModel(
    private val contract: TaskDetailContract,
    private val alarmManager: TaskNotificationScheduler,
    taskProvider: TaskDetailProvider
) : ViewModel() {

    val taskData = taskProvider.taskData

    val chipVisibility = MediatorLiveData<Boolean>()

    private val compositeDisposable = CompositeDisposable()

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

        taskData.value?.let {
            it.dueDate = alarm
            updateTask(it)
            alarmManager.scheduleTaskAlarm(it)
        }
        taskData.notify()
    }

    /**
     * Removes the task alarm.
     */
    fun removeAlarm() {
        Timber.d("removeAlarm()")

        taskData.value?.let {
            it.dueDate = null
            alarmManager.cancelTaskAlarm(it.id)
            updateTask(it)
        }
        taskData.notify()
    }

    private fun updateTask(task: Task) {
        Timber.d("updateTask() - $task")

        val disposable = contract.updateTask(task).subscribe()
        compositeDisposable.add(disposable)
    }
}
