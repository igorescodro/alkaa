package com.escodro.repository

import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import com.escodro.repository.datasource.TaskDataSource
import com.escodro.repository.mapper.TaskMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource,
    private val taskMapper: TaskMapper
) : TaskRepository {

    override suspend fun insertTask(task: Task) =
        taskDataSource.insertTask(taskMapper.toRepo(task))

    override suspend fun updateTask(task: Task) =
        taskDataSource.updateTask(taskMapper.toRepo(task))

    override suspend fun deleteTask(task: Task) =
        taskDataSource.deleteTask(taskMapper.toRepo(task))

    override suspend fun cleanTable() =
        taskDataSource.cleanTable()

    override suspend fun findAllTasksWithDueDate(): List<Task> =
        taskDataSource.findAllTasksWithDueDate().map { taskMapper.toDomain(it) }

    override suspend fun findTaskById(taskId: Long): Task =
        taskMapper.toDomain(taskDataSource.findTaskById(taskId))

    override fun findAllTasks(): Flow<List<Task>> =
        taskDataSource.findAllTasks().map { taskMapper.toDomain(it) }

    override suspend fun findTaskByTitle(title: String): Task =
        taskMapper.toDomain(taskDataSource.findTaskByTitle(title))
}
