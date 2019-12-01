package com.escodro.alarm.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.escodro.alarm.TaskReceiver
import com.escodro.domain.usecase.task.CompleteTask
import io.reactivex.Completable
import org.koin.core.inject
import timber.log.Timber

/**
 * [Worker] to set the Task alarms as completed.
 */
internal class TaskCompletedWorker(context: Context, params: WorkerParameters) :
    CompletableWorker(context, params) {

    private val completeTaskUseCase: CompleteTask by inject()

    override fun getObservable(): Completable {
        val taskId = inputData.getLong(TaskReceiver.EXTRA_TASK, 0)

        return completeTaskUseCase.test(taskId)
    }

    override fun onSuccess() {
        Timber.d("Task updated as completed")
    }

    override fun onError(error: Throwable) {
        Timber.d("onError: $error")
    }
}
