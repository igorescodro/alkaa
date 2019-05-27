package com.escodro.alkaa.ui.task.alarm.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.escodro.alkaa.di.provider.DaoProvider
import com.escodro.alkaa.ui.task.alarm.TaskReceiver
import io.reactivex.Single
import org.koin.core.inject
import timber.log.Timber

/**
 * [Worker] to set the Task alarms as completed.
 */
class TaskCompletedWorker(context: Context, params: WorkerParameters) :
    ObservableWorker<Unit>(context, params) {

    private val daoProvider: DaoProvider by inject()

    override fun getObservable(): Single<Unit> {
        val taskId = inputData.getLong(TaskReceiver.EXTRA_TASK, 0)

        return daoProvider.getTaskDao().getTaskById(taskId)
            .map {
                it.completed = true
                daoProvider.getTaskDao().updateTask(it)
            }
    }

    override fun onSuccess(result: Unit) {
        Timber.d("Task updated as completed")
    }

    override fun onError(error: Throwable) {
        Timber.d("onError: $error")
    }
}
