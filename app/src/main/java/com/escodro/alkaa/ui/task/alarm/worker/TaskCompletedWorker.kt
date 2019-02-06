package com.escodro.alkaa.ui.task.alarm.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.escodro.alkaa.di.provider.DaoProvider
import com.escodro.alkaa.ui.task.alarm.TaskReceiver
import io.reactivex.disposables.CompositeDisposable
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import timber.log.Timber
import java.util.concurrent.LinkedBlockingQueue

/**
 * [Worker] to set the Task alarms as completed.
 */
class TaskCompletedWorker(context: Context, params: WorkerParameters) :
    Worker(context, params), KoinComponent {

    private val daoProvider: DaoProvider by inject()

    private val compositeDisposable = CompositeDisposable()

    override fun doWork(): Result {
        Timber.d("doWork")

        val result = LinkedBlockingQueue<Result>()
        val taskId = inputData.getLong(TaskReceiver.EXTRA_TASK, 0)

        if (taskId == 0L) {
            return Result.success()
        }

        val disposable =
            daoProvider.getTaskDao().getTaskById(taskId)
                .map {
                    it.completed = true
                    daoProvider.getTaskDao().updateTask(it)
                }.subscribe(
                    {
                        Timber.d("Task updated as completed")
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

    override fun onStopped() {
        Timber.d("onStopped")
        compositeDisposable.clear()
    }
}
