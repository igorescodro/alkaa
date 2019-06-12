package com.escodro.domain.usecase.task

import com.escodro.domain.mapper.TaskMapper
import com.escodro.domain.viewdata.ViewData
import com.escodro.local.provider.DaoProvider

/**
 * Use case to add a task from the database.
 */
class AddTask(private val daoProvider: DaoProvider, private val mapper: TaskMapper) {

    /**
     * Adds a task.
     *
     * @param task the task to be added
     *
     * @return observable to be subscribe
     */
    operator fun invoke(task: ViewData.Task) {
        val entityTask = mapper.toEntityTask(task)
        return daoProvider.getTaskDao().insertTask(entityTask)
    }
}
