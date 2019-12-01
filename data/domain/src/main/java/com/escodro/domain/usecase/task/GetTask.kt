package com.escodro.domain.usecase.task

import com.escodro.core.extension.applySchedulers
import com.escodro.domain.mapper.TaskMapper
import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import com.escodro.domain.viewdata.ViewData
import com.escodro.local.provider.DaoProvider
import io.reactivex.Single

/**
 * Use case to get a task from the database.
 */
class GetTask(
    private val taskRepository: TaskRepository,
    private val daoProvider: DaoProvider,
    private val mapper: TaskMapper
) {

    /**
     * Gets a task.
     *
     * @param taskId the task id
     *
     * @return observable to be subscribe
     */
    operator fun invoke(taskId: Long): Single<ViewData.Task> =
        daoProvider.getTaskDao().getTaskById(taskId).map { mapper.toViewTask(it) }

    @Suppress("UndocumentedPublicFunction")
    fun test(taskId: Long): Single<Task> =
        taskRepository.findTaskById(taskId).applySchedulers()
}
