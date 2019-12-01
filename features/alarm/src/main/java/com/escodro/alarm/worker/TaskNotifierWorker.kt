package com.escodro.alarm.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.escodro.alarm.TaskReceiver
import com.escodro.alarm.mapper.TaskMapper
import com.escodro.alarm.model.Task
import com.escodro.alarm.notification.TaskNotification
import com.escodro.domain.usecase.task.GetTask
import io.reactivex.Single
import org.koin.core.inject
import timber.log.Timber

/**
 * [Worker] to process and show the Task alarms.
 */
internal class TaskNotifierWorker(context: Context, params: WorkerParameters) :
    SingleWorker<Task>(context, params) {

    private val taskNotification: TaskNotification by inject()

    private val getTaskUseCase: GetTask by inject()

    private val taskMapper: TaskMapper by inject()

    override fun getObservable(): Single<Task> {
        val taskId = inputData.getLong(TaskReceiver.EXTRA_TASK, 0)
        return getTaskUseCase.test(taskId).map { taskMapper.fromDomain(it) }
    }

    override fun onSuccess(result: Task) {
        if (result.isCompleted) {
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
