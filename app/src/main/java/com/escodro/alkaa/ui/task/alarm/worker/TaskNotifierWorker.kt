package com.escodro.alkaa.ui.task.alarm.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.escodro.alkaa.data.local.model.Task
import com.escodro.alkaa.di.provider.DaoProvider
import com.escodro.alkaa.ui.task.alarm.TaskReceiver
import com.escodro.alkaa.ui.task.alarm.notification.TaskNotification
import io.reactivex.Single
import org.koin.core.inject
import timber.log.Timber

/**
 * [Worker] to process and show the Task alarms.
 */
class TaskNotifierWorker(context: Context, params: WorkerParameters) :
    ObservableWorker<Task>(context, params) {

    private val taskNotification: TaskNotification by inject()

    private val daoProvider: DaoProvider by inject()

    override fun getObservable(): Single<Task> {
        val taskId = inputData.getLong(TaskReceiver.EXTRA_TASK, 0)
        return daoProvider.getTaskDao().getTaskById(taskId)
    }

    override fun onSuccess(result: Task) {
        if (result.completed) {
            Timber.d("Task '${result.title}' is already completed. Will not notify")
            return
        }

        Timber.d("Notifying task '${result.title}'")
        taskNotification.show(result)
    }

    override fun onError(error: Throwable) {
        Timber.d("onError: $error")
    }
}
