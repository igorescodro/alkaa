package com.escodro.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.escodro.domain.usecase.alarm.RescheduleFutureAlarms
import com.escodro.domain.usecase.alarm.ShowAlarm
import com.escodro.domain.usecase.task.CompleteTask
import com.escodro.domain.usecase.task.SnoozeTask
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

/**
 * [BroadcastReceiver] to be notified by the [android.app.AlarmManager].
 */
internal class TaskReceiver : BroadcastReceiver(), KoinComponent {

    private val completeTaskUseCase: CompleteTask by inject()

    private val showAlarmUseCase: ShowAlarm by inject()

    private val snoozeTaskUseCase: SnoozeTask by inject()

    private val rescheduleUseCase: RescheduleFutureAlarms by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.d("onReceive() - intent ${intent?.action}")

        when (intent?.action) {
            ALARM_ACTION -> onAlarm(intent)
            COMPLETE_ACTION -> onCompleteTask(intent)
            SNOOZE_ACTION -> onSnoozeTask(intent)
            Intent.ACTION_BOOT_COMPLETED -> onBootCompleted()
        }
    }

    private fun onAlarm(intent: Intent?) {
        Timber.d("onAlarm")

        val taskId = getTaskId(intent) ?: return
        GlobalScope.launch {
            showAlarmUseCase(taskId)
        }
    }

    private fun onCompleteTask(intent: Intent?) {
        Timber.d("onCompleteTask")

        val taskId = getTaskId(intent) ?: return
        GlobalScope.launch {
            completeTaskUseCase(taskId)
        }
    }

    private fun onSnoozeTask(intent: Intent?) {
        Timber.d("onSnoozeTask")

        val taskId = getTaskId(intent) ?: return
        GlobalScope.launch {
            snoozeTaskUseCase(taskId)
        }
    }

    private fun onBootCompleted() {
        Timber.d("onBootCompleted")

        GlobalScope.launch {
            rescheduleUseCase()
        }
    }

    private fun getTaskId(intent: Intent?) = intent?.getLongExtra(EXTRA_TASK, 0)

    companion object {

        const val EXTRA_TASK = "extra_task"

        const val ALARM_ACTION = "com.escodro.alkaa.SET_ALARM"

        const val COMPLETE_ACTION = "com.escodro.alkaa.SET_COMPLETE"

        const val SNOOZE_ACTION = "com.escodro.alkaa.SNOOZE"
    }
}
