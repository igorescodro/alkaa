package com.escodro.domain.usecase.task

import com.escodro.domain.mapper.TaskMapper
import com.escodro.domain.viewdata.ViewData
import com.escodro.local.provider.DaoProvider

/**
 * Use case to delete a task from the database.
 */
class DeleteTask(private val daoProvider: DaoProvider, private val mapper: TaskMapper) {

    /**
     * Deletes a task.
     *
     * @param task the task to be deleted
     *
     * @return observable to be subscribe
     */
    operator fun invoke(task: ViewData.Task) {
        val entityTask = mapper.toEntityTask(task)
        return daoProvider.getTaskDao().deleteTask(entityTask)
    }
}
