package com.escodro.alarm.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.escodro.alarm.mapper.TaskMapper
import com.escodro.alarm.notification.TaskNotificationScheduler
import com.escodro.domain.usecase.task.GetFutureTasks
import kotlinx.coroutines.coroutineScope
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

/**
 * [Worker] to reschedule the Task alarms.
 */
internal class TaskReschedulerWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params), KoinComponent {

    private val getFutureTasksUseCase: GetFutureTasks by inject()

    private val taskAlarmManager: TaskNotificationScheduler by inject()

    private val taskMapper: TaskMapper by inject()

    override suspend fun doWork(): Result = coroutineScope {
        getFutureTasksUseCase().map { taskMapper.fromDomain(it) }
            .forEach { task ->
                Timber.d("Task '${task.title} rescheduled to '${task.dueDate}")
                task.dueDate?.time?.time?.let { time ->
                    taskAlarmManager.scheduleTaskAlarm(task.id, time)
                }
            }
        Result.success()
    }
}
