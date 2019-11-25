package com.escodro.local.datasource

import com.escodro.local.mapper.TaskMapper
import com.escodro.local.provider.DaoProvider
import com.escodro.repository.datasource.TaskDataSource
import com.escodro.repository.model.Task
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Local implementation of [TaskDataSource].
 */
internal class TaskLocalDataSource(daoProvider: DaoProvider, private val taskMapper: TaskMapper) :
    TaskDataSource {

    private val taskDao = daoProvider.getTaskDao()

    override fun insertTask(task: Task): Completable =
        taskDao.insertTask(taskMapper.fromRepo(task))

    override fun updateTask(task: Task): Completable =
        taskDao.updateTask(taskMapper.fromRepo(task))

    override fun deleteTask(task: Task): Completable =
        taskDao.deleteTask(taskMapper.fromRepo(task))

    override fun cleanTable(): Completable =
        taskDao.cleanTable()

    override fun findAllTasksWithDueDate(): Single<List<Task>> =
        taskDao.getAllTasksWithDueDate().map { taskMapper.toRepo(it) }

    override fun findAllTasks(): Flowable<List<Task>> =
        taskDao.getAllTasks().map { taskMapper.toRepo(it) }

    override fun findTaskByTitle(title: String): Single<Task> =
        taskDao.findTaskByTitle(title).map { taskMapper.toRepo(it) }
}
