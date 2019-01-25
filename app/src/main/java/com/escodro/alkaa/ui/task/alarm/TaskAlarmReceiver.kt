package com.escodro.alkaa.ui.task.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.escodro.alkaa.common.extension.unmarshallParcelable
import com.escodro.alkaa.data.local.model.Task
import com.escodro.alkaa.ui.task.notification.TaskNotification
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import timber.log.Timber

/**
 * [BroadcastReceiver] to be notified by the [android.app.AlarmManager].
 */
class TaskAlarmReceiver : BroadcastReceiver(), KoinComponent {

    private val taskNotification: TaskNotification by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.d("onReceive() - intent ${intent?.action}")

        when (intent?.action) {
            TaskAlarmManager.ALARM_ACTION -> onAlarm(intent)
            Intent.ACTION_BOOT_COMPLETED -> onBootCompleted()
        }
    }

    private fun onBootCompleted() {
        Timber.d("onBootCompleted")

        val worker = OneTimeWorkRequest.Builder(TaskAlarmWorker::class.java).build()
        WorkManager.getInstance().enqueue(worker)
    }

    private fun onAlarm(intent: Intent?) {

        val byte = intent?.getByteArrayExtra(TaskAlarmManager.EXTRA_TASK) ?: return
        val task = unmarshallParcelable<Task>(byte) ?: return

        Timber.d("Notifying task '${task.title}'")
        taskNotification.show(task)
    }
}
