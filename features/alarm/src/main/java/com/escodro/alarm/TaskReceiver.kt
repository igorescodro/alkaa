package com.escodro.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.escodro.domain.usecase.alarm.RescheduleFutureAlarms
import com.escodro.domain.usecase.alarm.ShowAlarm
import com.escodro.domain.usecase.alarm.SnoozeAlarm
import com.escodro.domain.usecase.task.CompleteTask
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * [BroadcastReceiver] to be notified by the [android.app.AlarmManager].
 */
@AndroidEntryPoint
internal class TaskReceiver @Inject constructor(
    private val completeTaskUseCase: CompleteTask,
    private val showAlarmUseCase: ShowAlarm,
    private val snoozeAlarmUseCase: SnoozeAlarm,
    private val rescheduleUseCase: RescheduleFutureAlarms,
) : BroadcastReceiver() {

    @Suppress("GlobalCoroutineUsage")
    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.d("onReceive() - intent ${intent?.action}")

        GlobalScope.launch {
            handleIntent(intent)
        }
    }

    private suspend fun handleIntent(intent: Intent?) =
        when (intent?.action) {
            ALARM_ACTION -> getTaskId(intent)?.let { showAlarmUseCase(it) }
            COMPLETE_ACTION -> getTaskId(intent)?.let { completeTaskUseCase(it) }
            SNOOZE_ACTION -> getTaskId(intent)?.let { snoozeAlarmUseCase(it) }
            Intent.ACTION_BOOT_COMPLETED -> rescheduleUseCase()
            else -> Timber.e("Action not supported")
        }

    private fun getTaskId(intent: Intent?) = intent?.getLongExtra(EXTRA_TASK, 0)

    companion object {

        const val EXTRA_TASK = "extra_task"

        const val ALARM_ACTION = "com.escodro.alkaa.SET_ALARM"

        const val COMPLETE_ACTION = "com.escodro.alkaa.SET_COMPLETE"

        const val SNOOZE_ACTION = "com.escodro.alkaa.SNOOZE"
    }
}
