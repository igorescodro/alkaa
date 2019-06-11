package com.escodro.alarm.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.escodro.alarm.TaskReceiver
import com.escodro.domain.usecase.task.SnoozeTask
import io.reactivex.Single
import org.koin.core.inject
import timber.log.Timber

/**
 * [Worker] to snooze Task alarms as completed.
 */
class TaskSnoozeWorker(context: Context, params: WorkerParameters) :
    ObservableWorker<Unit>(context, params) {

    private val snoozeTaskUseCase: SnoozeTask by inject()

    override fun getObservable(): Single<Unit> {
        val taskId = inputData.getLong(TaskReceiver.EXTRA_TASK, 0)

        return snoozeTaskUseCase(taskId, SNOOZE_MINUTES)
    }

    override fun onSuccess(result: Unit) {
        Timber.d("Task updated as completed")
    }

    override fun onError(error: Throwable) {
        Timber.d("onError: $error")
    }

    companion object {

        private const val SNOOZE_MINUTES = 15
    }
}
