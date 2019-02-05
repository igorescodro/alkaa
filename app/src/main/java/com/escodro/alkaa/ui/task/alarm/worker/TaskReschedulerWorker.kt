package com.escodro.alkaa.ui.task.alarm.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.escodro.alkaa.common.extension.applySchedulers
import com.escodro.alkaa.data.local.model.Task
import com.escodro.alkaa.di.provider.DaoProvider
import com.escodro.alkaa.ui.task.alarm.notification.TaskNotificationScheduler
import io.reactivex.disposables.CompositeDisposable
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import timber.log.Timber
import java.util.Calendar
import java.util.concurrent.LinkedBlockingQueue

/**
 * [Worker] to reschedule the Task alarms.
 */
class TaskReschedulerWorker(context: Context, params: WorkerParameters) :
    Worker(context, params), KoinComponent {

    private val daoProvider: DaoProvider by inject()

    private val taskAlarmManager: TaskNotificationScheduler by inject()

    private val compositeDisposable = CompositeDisposable()

    override fun doWork(): Result {
        Timber.d("doWork")

        val result = LinkedBlockingQueue<Result>()

        val disposable = daoProvider.getTaskDao()
            .getAllTasksWithDueDate()
            .flattenAsObservable { it }
            .filter { isInFuture(it.dueDate) }
            .toList()
            .applySchedulers()
            .subscribe(
                {
                    onSuccess(it)
                    result.put(Result.success())
                },
                {
                    Timber.e(it)
                    result.put(Result.failure())
                }
            )

        compositeDisposable.add(disposable)

        return try {
            result.take()
        } catch (e: InterruptedException) {
            Result.retry()
        }
    }

    private fun onSuccess(taskList: List<Task>) {
        taskList.forEach {
            taskAlarmManager.scheduleTaskAlarm(it)
        }
    }

    private fun isInFuture(calendar: Calendar?): Boolean {
        val currentTime = Calendar.getInstance()
        return calendar?.after(currentTime) ?: false
    }

    override fun onStopped() {
        Timber.d("onStopped")
        compositeDisposable.clear()
    }
}
