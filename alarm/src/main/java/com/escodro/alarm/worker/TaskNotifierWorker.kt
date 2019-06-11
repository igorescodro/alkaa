package com.escodro.alarm.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.escodro.alarm.TaskReceiver
import com.escodro.alarm.notification.TaskNotification
import com.escodro.domain.usecase.task.GetTask
import com.escodro.domain.viewdata.ViewData
import io.reactivex.Single
import org.koin.core.inject
import timber.log.Timber

/**
 * [Worker] to process and show the Task alarms.
 */
class TaskNotifierWorker(context: Context, params: WorkerParameters) :
    ObservableWorker<ViewData.Task>(context, params) {

    private val taskNotification: TaskNotification by inject()

    private val getTaskUseCase: GetTask by inject()

    override fun getObservable(): Single<ViewData.Task> {
        val taskId = inputData.getLong(TaskReceiver.EXTRA_TASK, 0)
        return getTaskUseCase(taskId)
    }

    override fun onSuccess(result: ViewData.Task) {
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
