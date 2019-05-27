package com.escodro.alkaa.ui.task.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.escodro.alkaa.common.extension.getNotificationManager
import com.escodro.alkaa.ui.task.alarm.worker.TaskCompletedWorker
import com.escodro.alkaa.ui.task.alarm.worker.TaskNotifierWorker
import com.escodro.alkaa.ui.task.alarm.worker.TaskReschedulerWorker
import com.escodro.alkaa.ui.task.alarm.worker.TaskSnoozeWorker
import org.koin.core.KoinComponent
import timber.log.Timber

/**
 * [BroadcastReceiver] to be notified by the [android.app.AlarmManager].
 */
class TaskReceiver : BroadcastReceiver(), KoinComponent {

    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.d("onReceive() - intent ${intent?.action}")

        when (intent?.action) {
            ALARM_ACTION -> onAlarm(intent)
            COMPLETE_ACTION -> onCompleteTask(context, intent)
            SNOOZE_ACTION -> onSnoozeTask(context, intent)
            Intent.ACTION_BOOT_COMPLETED -> onBootCompleted()
        }
    }

    private fun onAlarm(intent: Intent?) {
        Timber.d("onAlarm")

        val taskId = getTaskId(intent) ?: return
        val worker = OneTimeWorkRequest.Builder(TaskNotifierWorker::class.java)
        executeWorker(taskId, worker)
    }

    private fun onCompleteTask(context: Context?, intent: Intent?) {
        Timber.d("onCompleteTask")

        val taskId = getTaskId(intent) ?: return
        val worker = OneTimeWorkRequest.Builder(TaskCompletedWorker::class.java)
        executeWorker(taskId, worker)
        context?.getNotificationManager()?.cancel(taskId.toInt())
    }

    private fun onSnoozeTask(context: Context?, intent: Intent?) {
        Timber.d("onSnoozeTask")

        val taskId = getTaskId(intent) ?: return
        val worker = OneTimeWorkRequest.Builder(TaskSnoozeWorker::class.java)
        executeWorker(taskId, worker)
        context?.getNotificationManager()?.cancel(taskId.toInt())
    }

    private fun onBootCompleted() {
        Timber.d("onBootCompleted")

        val worker = OneTimeWorkRequest.Builder(TaskReschedulerWorker::class.java).build()
        WorkManager.getInstance().enqueue(worker)
    }

    private fun getTaskId(intent: Intent?) = intent?.getLongExtra(EXTRA_TASK, 0)

    private fun executeWorker(taskId: Long, worker: OneTimeWorkRequest.Builder) {
        val inputData = Data.Builder().putLong(EXTRA_TASK, taskId).build()
        worker.setInputData(inputData)
        WorkManager.getInstance().enqueue(worker.build())

        Timber.d("executeWorker - Worker enqueued: $worker")
    }

    companion object {

        const val EXTRA_TASK = "extra_task"

        const val ALARM_ACTION = "com.escodro.alkaa.SET_ALARM"

        const val COMPLETE_ACTION = "com.escodro.alkaa.SET_COMPLETE"

        const val SNOOZE_ACTION = "com.escodro.alkaa.SNOOZE"
    }
}
