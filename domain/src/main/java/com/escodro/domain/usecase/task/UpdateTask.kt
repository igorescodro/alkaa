package com.escodro.domain.usecase.task

import com.escodro.domain.mapper.TaskMapper
import com.escodro.domain.viewdata.ViewData
import com.escodro.local.provider.DaoProvider

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
    operator fun invoke(task: ViewData.Task) {
        val entityTask = mapper.toEntityTask(task)
        return daoProvider.getTaskDao().updateTask(entityTask)
    }
}
