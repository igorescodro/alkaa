package com.escodro.domain.usecase.taskwithcategory

import com.escodro.core.extension.applySchedulers
import com.escodro.domain.mapper.TaskWithCategoryMapper
import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.repository.TaskWithCategoryRepository
import com.escodro.domain.viewdata.ViewData
import com.escodro.local.provider.DaoProvider
import io.reactivex.Flowable

/**
 * Use case to get all completed tasks from the database.
 */
class LoadCompletedTasks(
    private val repository: TaskWithCategoryRepository,
    private val daoProvider: DaoProvider,
    private val mapper: TaskWithCategoryMapper
) {

    /**
     * Gets all completed tasks.
     *
     * @return observable to be subscribe
     */
    operator fun invoke(): Flowable<List<ViewData.TaskWithCategory>> =
        daoProvider.getTaskWithCategoryDao()
            .findAllTasksWithCategory(isCompleted = true)
            .map { mapper.toViewTask(it) }
            .applySchedulers()

    @Suppress("UndocumentedPublicFunction")
    fun test(): Flowable<List<TaskWithCategory>> =
        repository.findAllTasksWithCategory(isCompleted = true).applySchedulers()
}
