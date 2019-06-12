package com.escodro.domain.mapper

import com.escodro.domain.viewdata.ViewData
import com.escodro.model.Task
import com.escodro.model.TaskWithCategory

/**
 * Converts between the [TaskWithCategory] model from the database and [ViewData.TaskWithCategory]
 * UI object.
 */
class TaskWithCategoryMapper(
    private val taskMapper: TaskMapper,
    private val categoryMapper: CategoryMapper
) {

    /**
     * Maps from a [List] of [Task] to [List] of [ViewData.Task].
     *
     * @param task list to be mapped
     *
     * @return the converted list
     */
    fun toViewTask(taskList: List<TaskWithCategory>) =
        taskList.map { toViewTask(it) }

    /**
     * Maps from a [TaskWithCategory] with category to [ViewData.TaskWithCategory].
     *
     * @param task object to be mapped
     *
     * @return the converted object
     */
    fun toViewTask(taskWithCategory: TaskWithCategory) =
        ViewData.TaskWithCategory(
            task = taskMapper.toViewTask(taskWithCategory.task),
            category = taskWithCategory.category?.let { categoryMapper.toViewCategory(it) }
        )

    /**
     * Maps from a [ViewData.TaskWithCategory] to [TaskWithCategory].
     *
     * @param task object to be mapped
     *
     * @return the converted object
     */
    fun toEntityTask(taskWithCategory: ViewData.TaskWithCategory) =
        TaskWithCategory(
            task = taskMapper.toEntityTask(taskWithCategory.task),
            category = taskWithCategory.category?.let { categoryMapper.toEntityCategory(it) }
        )
}
