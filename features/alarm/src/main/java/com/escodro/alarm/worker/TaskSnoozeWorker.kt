package com.escodro.alarm.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.escodro.alarm.TaskReceiver
import com.escodro.domain.usecase.task.SnoozeTask
import io.reactivex.Completable
import org.koin.core.inject
import timber.log.Timber

/**
 * [Worker] to snooze Task alarms as completed.
 */
internal class TaskSnoozeWorker(context: Context, params: WorkerParameters) :
    CompletableWorker(context, params) {

    private val snoozeTaskUseCase: SnoozeTask by inject()

    override fun getObservable(): Completable {
        val taskId = inputData.getLong(TaskReceiver.EXTRA_TASK, 0)

        return snoozeTaskUseCase(taskId, SNOOZE_MINUTES)
    }

    override fun onSuccess() {
        Timber.d("Task updated as completed")
    }

    override fun onError(error: Throwable) {
        Timber.d("onError: $error")
    }

    companion object {

        private const val SNOOZE_MINUTES = 15
    }
}
