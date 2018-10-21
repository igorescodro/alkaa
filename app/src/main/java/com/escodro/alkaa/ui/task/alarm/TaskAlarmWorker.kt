package com.escodro.alkaa.ui.task.alarm

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.escodro.alkaa.common.extension.applySchedulers
import com.escodro.alkaa.data.local.model.Task
import com.escodro.alkaa.di.provider.DaoProvider
import io.reactivex.disposables.CompositeDisposable
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import timber.log.Timber
import java.util.Calendar
import java.util.concurrent.LinkedBlockingQueue

/**
 * [Worker] to reschedule the Task alarms.
 */
class TaskAlarmWorker(context: Context, params: WorkerParameters) :
    Worker(context, params), KoinComponent {

    private val daoProvider: DaoProvider by inject()

    private val taskAlarmManager: TaskAlarmManager by inject()

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
                    result.put(Result.SUCCESS)
                },
                {
                    Timber.e(it)
                    result.put(Result.FAILURE)
                }
            )

        compositeDisposable.add(disposable)

        return try {
            result.take()
        } catch (e: InterruptedException) {
            Result.RETRY
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

    override fun onStopped(cancelled: Boolean) {
        Timber.d("onStopped")
        compositeDisposable.clear()
    }
}
