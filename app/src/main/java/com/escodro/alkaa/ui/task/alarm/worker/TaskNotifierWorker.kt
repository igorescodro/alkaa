package com.escodro.alkaa.ui.task.alarm.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.escodro.alkaa.data.local.model.Task
import com.escodro.alkaa.di.provider.DaoProvider
import com.escodro.alkaa.ui.task.alarm.TaskReceiver
import com.escodro.alkaa.ui.task.alarm.notification.TaskNotification
import io.reactivex.disposables.CompositeDisposable
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import timber.log.Timber
import java.util.concurrent.LinkedBlockingQueue

/**
 * [Worker] to process and show the Task alarms.
 */
class TaskNotifierWorker(context: Context, params: WorkerParameters) :
    Worker(context, params), KoinComponent {

    private val daoProvider: DaoProvider by inject()

    private val taskNotification: TaskNotification by inject()

    private val compositeDisposable = CompositeDisposable()

    override fun doWork(): Result {
        Timber.d("doWork")

        val result = LinkedBlockingQueue<Result>()
        val taskId = inputData.getLong(TaskReceiver.EXTRA_TASK, 0)

        if (taskId == 0L) {
            return Result.success()
        }

        val disposable = daoProvider.getTaskDao().getTaskById(taskId).subscribe(
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

    private fun onSuccess(task: Task) {
        if (task.completed) {
            Timber.d("Task '${task.title}' is already completed. Will not notify")
            return
        }

        Timber.d("Notifying task '${task.title}'")
        taskNotification.show(task)
    }

    override fun onStopped() {
        Timber.d("onStopped")
        compositeDisposable.clear()
    }
}
