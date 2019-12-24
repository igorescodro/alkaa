package com.escodro.alarm.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.escodro.alarm.TaskReceiver
import com.escodro.domain.usecase.task.SnoozeTask
import kotlinx.coroutines.coroutineScope
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

/**
 * [Worker] to snooze Task alarms as completed.
 */
internal class TaskSnoozeWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params), KoinComponent {

    private val snoozeTaskUseCase: SnoozeTask by inject()

    override suspend fun doWork(): Result = coroutineScope {
        val taskId = inputData.getLong(TaskReceiver.EXTRA_TASK, 0)

        snoozeTaskUseCase(taskId, SNOOZE_MINUTES)
        Timber.d("Task updated as completed")
        Result.success()
    }

    companion object {

        private const val SNOOZE_MINUTES = 15
    }
}
