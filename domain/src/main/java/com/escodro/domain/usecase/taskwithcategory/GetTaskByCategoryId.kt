package com.escodro.domain.usecase.taskwithcategory

import com.escodro.core.extension.applySchedulers
import com.escodro.domain.mapper.TaskWithCategoryMapper
import com.escodro.domain.viewdata.ViewData
import com.escodro.local.provider.DaoProvider
import io.reactivex.Flowable

/**
 * Use case to get a task with category by the category id from the database.
 */
class GetTaskByCategoryId(
    private val daoProvider: DaoProvider,
    private val mapper: TaskWithCategoryMapper
) {

    /**
     * Gets a task with category by the category id.
     *
     * @param categoryId the category id
     *
     * @return observable to be subscribe
     */
    operator fun invoke(categoryId: Long): Flowable<List<ViewData.TaskWithCategory>> =
        daoProvider.getTaskWithCategoryDao()
            .getAllTasksWithCategoryId(categoryId)
            .map { mapper.toViewTask(it) }
            .applySchedulers()
}
