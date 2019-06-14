package com.escodro.domain.usecase.task

import com.escodro.core.extension.applySchedulers
import com.escodro.domain.mapper.TaskMapper
import com.escodro.domain.viewdata.ViewData
import com.escodro.local.provider.DaoProvider
import io.reactivex.Completable

/**
 * Use case to update a task from the database.
 */
class UpdateTask(private val daoProvider: DaoProvider, private val mapper: TaskMapper) {

    /**
     * Updates a task.
     *
     * @param task the task to be updated
     *
     * @return observable to be subscribe
     */
    operator fun invoke(task: ViewData.Task): Completable {
        val entityTask = mapper.toEntityTask(task)
        return daoProvider.getTaskDao().updateTask(entityTask).applySchedulers()
    }
}
