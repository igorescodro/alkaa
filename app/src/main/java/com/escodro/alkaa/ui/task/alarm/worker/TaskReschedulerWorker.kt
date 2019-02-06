package com.escodro.alkaa.ui.task.alarm.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.escodro.alkaa.data.local.model.Task
import com.escodro.alkaa.di.provider.DaoProvider
import com.escodro.alkaa.ui.task.alarm.notification.TaskNotificationScheduler
import io.reactivex.Single
import org.koin.standalone.inject
import timber.log.Timber
import java.util.Calendar

/**
 * [Worker] to reschedule the Task alarms.
 */
class TaskReschedulerWorker(context: Context, params: WorkerParameters) :
    ObservableWorker<MutableList<Task>>(context, params) {

    private val daoProvider: DaoProvider by inject()

    private val taskAlarmManager: TaskNotificationScheduler by inject()

    override fun getObservable(): Single<MutableList<Task>> =
        daoProvider.getTaskDao()
            .getAllTasksWithDueDate()
            .flattenAsObservable { it }
            .filter { isInFuture(it.dueDate) }
            .toList()

    override fun onSuccess(result: MutableList<Task>) {
        result.forEach {
            taskAlarmManager.scheduleTaskAlarm(it)
        }
    }

    override fun onError(error: Throwable) {
        Timber.d("onError: $error")
    }

    private fun isInFuture(calendar: Calendar?): Boolean {
        val currentTime = Calendar.getInstance()
        return calendar?.after(currentTime) ?: false
    }
}
