package com.escodro.local.datasource

import com.escodro.local.dao.TaskDao
import com.escodro.local.mapper.TaskMapper
import com.escodro.repository.datasource.TaskDataSource
import com.escodro.repository.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Local implementation of [TaskDataSource].
 */
internal class TaskLocalDataSource(
    private val taskDao: TaskDao,
    private val taskMapper: TaskMapper,
) : TaskDataSource {

    override suspend fun insertTask(task: Task): Long =
        taskDao.insertTask(taskMapper.toLocal(task))

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(taskMapper.toLocal(task))
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(taskMapper.toLocal(task))
    }

    override suspend fun cleanTable() {
        taskDao.cleanTable()
    }

    override suspend fun findAllTasksWithDueDate(): List<Task> =
        taskDao.findAllTasksWithDueDate().map { taskMapper.toRepo(it) }

    override suspend fun findTaskById(taskId: Long): Task? =
        taskDao.getTaskById(taskId)?.let { taskMapper.toRepo(it) }

    override fun findTaskByIdFlow(taskId: Long): Flow<Task?> =
        taskDao.getTaskByIdFlow(taskId).map { task ->
            task?.let { taskMapper.toRepo(it) }
        }
}
