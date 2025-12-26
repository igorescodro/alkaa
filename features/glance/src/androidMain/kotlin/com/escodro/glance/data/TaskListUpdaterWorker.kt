package com.escodro.glance.data

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.escodro.glance.model.Task
import com.escodro.glance.presentation.TaskListGlanceWidget
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Worker to update the Task List in the widget. A worker is needed here because the process might
 * be dead during between the writing, reading and displaying of the information.
 */
internal class TaskListUpdaterWorker(
    private val context: Context,
    workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters), KoinComponent {

    private val glanceUpdater: TaskListGlanceUpdater by inject()

    override suspend fun doWork(): Result {
        val list = glanceUpdater.loadTaskList().first()
        updateWidgets(list)
        return Result.success()
    }

    private suspend fun updateWidgets(list: List<Task>) {
        TaskListStateDefinition.updateData(context, list)
        TaskListGlanceWidget().updateAll(context)
    }

    companion object {
        private val uniqueWorkName = TaskListUpdaterWorker::class.java.simpleName

        fun enqueue(context: Context) {
            val manager = WorkManager.getInstance(context)
            val requestBuilder = OneTimeWorkRequestBuilder<TaskListUpdaterWorker>()

            manager.enqueueUniqueWork(
                uniqueWorkName,
                ExistingWorkPolicy.KEEP,
                requestBuilder.build(),
            )
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(uniqueWorkName)
        }
    }
}
