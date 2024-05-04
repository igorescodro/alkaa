package com.escodro.local.dao.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.escodro.local.Task
import com.escodro.local.TaskQueries
import com.escodro.local.dao.TaskDao
import com.escodro.local.provider.DatabaseProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.first

internal class TaskDaoImpl(private val databaseProvider: DatabaseProvider) : TaskDao {

    private val taskQueries: TaskQueries
        get() = databaseProvider.getInstance().taskQueries

    override suspend fun insertTask(task: Task): Long {
        val id: Long? = task.task_id.takeIf { it > 0L } // Instrumentation test to force id
        return taskQueries.transactionWithResult {
            taskQueries.insert(
                task_id = id,
                task_is_completed = task.task_is_completed,
                task_title = task.task_title,
                task_description = task.task_description,
                task_category_id = task.task_category_id,
                task_due_date = task.task_due_date,
                task_creation_date = task.task_creation_date,
                task_completed_date = task.task_completed_date,
                task_is_repeating = task.task_is_repeating,
                task_alarm_interval = task.task_alarm_interval,
            )
            taskQueries.lastInsertedId().executeAsOne()
        }
    }

    override suspend fun updateTask(task: Task) {
        with(task) {
            taskQueries.update(
                task_is_completed = task_is_completed,
                task_title = task_title,
                task_description = task_description,
                task_category_id = task_category_id,
                task_due_date = task_due_date,
                task_creation_date = task_creation_date,
                task_completed_date = task_completed_date,
                task_is_repeating = task_is_repeating,
                task_alarm_interval = task_alarm_interval,
                task_id = task_id,
            )
        }
    }

    override suspend fun deleteTask(task: Task) {
        taskQueries.delete(task.task_id)
    }

    override suspend fun cleanTable() {
        taskQueries.cleanTable()
    }

    override suspend fun findAllTasksWithDueDate(): List<Task> =
        taskQueries.selectAllTasksWithDueDate(mapper = ::Task)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .first()

    override suspend fun getTaskById(taskId: Long): Task? =
        taskQueries.selectTaskById(taskId).executeAsOneOrNull()
}
