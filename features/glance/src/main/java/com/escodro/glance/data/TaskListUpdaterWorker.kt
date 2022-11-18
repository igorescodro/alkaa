package com.escodro.glance.data

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.escodro.glance.model.Task
import com.escodro.glance.presentation.TaskListGlanceWidget
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Worker to update the Task List in the widget. A worker is needed here because the process might
 * be dead during between the writing, reading and displaying of the information.
 */
internal class TaskListUpdaterWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters), KoinComponent {

    private val viewModel: TaskListGlanceUpdater by inject()

    override suspend fun doWork(): Result {
        val manager = GlanceAppWidgetManager(context)
        val glanceIds = manager.getGlanceIds(TaskListGlanceWidget::class.java)
        val list = viewModel.loadTaskList().first()
        updateWidgets(glanceIds, list)
        return Result.success()
    }

    private suspend fun updateWidgets(glanceIds: List<GlanceId>, list: List<Task>) {
        glanceIds.forEach {
            updateAppWidgetState(
                context = context,
                definition = TaskListStateDefinition,
                glanceId = it,
                updateState = { list.toImmutableList() }
            )
            TaskListGlanceWidget().updateAll(context)
        }
    }

    companion object {
        private val uniqueWorkName = TaskListUpdaterWorker::class.java.simpleName

        fun enqueue(context: Context) {
            val manager = WorkManager.getInstance(context)
            val requestBuilder = OneTimeWorkRequestBuilder<TaskListUpdaterWorker>()

            manager.enqueueUniqueWork(
                uniqueWorkName,
                ExistingWorkPolicy.KEEP,
                requestBuilder.build()
            )
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(uniqueWorkName)
        }
    }
}
