package com.escodro.alarm.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.escodro.alarm.TaskMapper
import com.escodro.alarm.model.Task
import com.escodro.alarm.notification.TaskNotificationScheduler
import com.escodro.domain.usecase.task.GetFutureTasks
import io.reactivex.Single
import org.koin.core.inject
import timber.log.Timber

/**
 * [Worker] to reschedule the Task alarms.
 */
internal class TaskReschedulerWorker(context: Context, params: WorkerParameters) :
    SingleWorker<List<Task>>(context, params) {

    private val getFutureTasksUseCase: GetFutureTasks by inject()

    private val taskAlarmManager: TaskNotificationScheduler by inject()

    private val taskMapper: TaskMapper by inject()

    override fun getObservable(): Single<List<Task>> =
        getFutureTasksUseCase.test().map { taskMapper.fromDomain(it) }

    override fun onSuccess(result: List<Task>) {
        result.forEach {
            Timber.d("Task '${it.title} rescheduled to '${it.dueDate}")
            taskAlarmManager.scheduleTaskAlarm(it.id, it.dueDate?.time?.time)
        }
    }

    override fun onError(error: Throwable) {
        Timber.d("onError: $error")
    }
}
