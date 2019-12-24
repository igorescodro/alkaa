package com.escodro.alarm.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.escodro.alarm.TaskReceiver
import com.escodro.alarm.mapper.TaskMapper
import com.escodro.alarm.notification.TaskNotification
import com.escodro.domain.usecase.task.GetTask
import kotlinx.coroutines.coroutineScope
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

/**
 * [Worker] to process and show the Task alarms.
 */
internal class TaskNotifierWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params), KoinComponent {

    private val taskNotification: TaskNotification by inject()

    private val getTaskUseCase: GetTask by inject()

    private val taskMapper: TaskMapper by inject()

    override suspend fun doWork(): Result = coroutineScope {
        val taskId = inputData.getLong(TaskReceiver.EXTRA_TASK, 0)
        val task = taskMapper.fromDomain(getTaskUseCase(taskId))

        if (task.isCompleted) {
            Timber.d("Task '${task.title}' is already completed. Will not notify")
        } else {
            Timber.d("Notifying task '${task.title}'")
            taskNotification.show(task)
        }

        Result.success()
    }
}
