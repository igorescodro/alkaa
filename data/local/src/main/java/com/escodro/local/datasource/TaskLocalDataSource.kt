package com.escodro.local.datasource

import com.escodro.local.mapper.TaskMapper
import com.escodro.local.provider.DaoProvider
import com.escodro.repository.datasource.TaskDataSource
import com.escodro.repository.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Local implementation of [TaskDataSource].
 */
internal class TaskLocalDataSource(daoProvider: DaoProvider, private val taskMapper: TaskMapper) :
    TaskDataSource {

    private val taskDao = daoProvider.getTaskDao()

    override suspend fun insertTask(task: Task) =
        taskDao.insertTask(taskMapper.toLocal(task))

    override suspend fun updateTask(task: Task) =
        taskDao.updateTask(taskMapper.toLocal(task))

    override suspend fun deleteTask(task: Task) =
        taskDao.deleteTask(taskMapper.toLocal(task))

    override suspend fun cleanTable() =
        taskDao.cleanTable()

    override suspend fun findAllTasksWithDueDate(): List<Task> =
        taskDao.findAllTasksWithDueDate().map { taskMapper.toRepo(it) }

    override fun findTaskById(taskId: Long): Flow<Task> =
        taskDao.getTaskById(taskId).map { taskMapper.toRepo(it) }
}
