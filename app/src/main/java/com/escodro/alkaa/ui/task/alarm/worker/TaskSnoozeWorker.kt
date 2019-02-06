package com.escodro.alkaa.ui.task.alarm.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.escodro.alkaa.data.local.model.Task
import com.escodro.alkaa.di.provider.DaoProvider
import com.escodro.alkaa.ui.task.alarm.TaskReceiver
import io.reactivex.Single
import org.koin.standalone.inject
import timber.log.Timber
import java.util.Calendar

/**
 * [Worker] to snooze Task alarms as completed.
 */
class TaskSnoozeWorker(context: Context, params: WorkerParameters) :
    ObservableWorker<Unit>(context, params) {

    private val daoProvider: DaoProvider by inject()

    override fun getObservable(): Single<Unit> {
        val taskId = inputData.getLong(TaskReceiver.EXTRA_TASK, 0)

        return daoProvider.getTaskDao().getTaskById(taskId)
            .map { snoozeAlarm(it) }
    }

    override fun onSuccess(result: Unit) {
        Timber.d("Task updated as completed")
    }

    override fun onError(error: Throwable) {
        Timber.d("onError: $error")
    }

    private fun snoozeAlarm(task: Task) {
        task.dueDate?.add(Calendar.MINUTE, SNOOZE_MINUTES)
        daoProvider.getTaskDao().updateTask(task)
    }

    companion object {

        private const val SNOOZE_MINUTES = 15
    }
}
