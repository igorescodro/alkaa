package com.escodro.task.mapper

import com.escodro.domain.model.TaskWithCategory as DomainTaskWithCategory
import com.escodro.task.model.TaskWithCategory as ViewTaskWithCategory

/**
 * Maps Task With Category between View and Domain.
 */
internal class TaskWithCategoryMapper(
    private val taskMapper: TaskMapper,
    private val categoryMapper: CategoryMapper
) {

    /**
     * Maps Task With Category from Domain to View.
     *
     * @param localTaskList the list of Task With Category to be converted.
     *
     * @return the converted list of Task With Category
     */
    fun toView(localTaskList: List<DomainTaskWithCategory>): List<ViewTaskWithCategory> =
        localTaskList.map { toView(it) }

    private fun toView(localTask: DomainTaskWithCategory): ViewTaskWithCategory =
        ViewTaskWithCategory(
            task = taskMapper.toView(localTask.task),
            category = localTask.category?.let { categoryMapper.toView(it) }
        )
}
