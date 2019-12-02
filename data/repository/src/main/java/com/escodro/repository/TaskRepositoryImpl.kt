package com.escodro.repository

import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import com.escodro.repository.datasource.TaskDataSource
import com.escodro.repository.mapper.TaskMapper
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

internal class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource,
    private val taskMapper: TaskMapper
) : TaskRepository {

    override fun insertTask(task: Task): Completable =
        taskDataSource.insertTask(taskMapper.toRepo(task))

    override fun updateTask(task: Task): Completable =
        taskDataSource.updateTask(taskMapper.toRepo(task))

    override fun deleteTask(task: Task): Completable =
        taskDataSource.deleteTask(taskMapper.toRepo(task))

    override fun cleanTable(): Completable =
        taskDataSource.cleanTable()

    override fun findAllTasksWithDueDate(): Single<List<Task>> =
        taskDataSource.findAllTasksWithDueDate().map { taskMapper.toDomain(it) }

    override fun findTaskById(taskId: Long): Single<Task> =
        taskDataSource.findTaskById(taskId).map { taskMapper.toDomain(it) }

    override fun findAllTasks(): Flowable<List<Task>> =
        taskDataSource.findAllTasks().map { taskMapper.toDomain(it) }

    override fun findTaskByTitle(title: String): Single<Task> =
        taskDataSource.findTaskByTitle(title).map { taskMapper.toDomain(it) }
}
