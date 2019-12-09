package com.escodro.local.mapper

import com.escodro.local.model.TaskWithCategory as LocalTaskWithCategory
import com.escodro.repository.model.TaskWithCategory as RepoTaskWithCategory

/**
 * Maps Task With Category between Repository and Local.
 */
internal class TaskWithCategoryMapper(
    private val taskMapper: TaskMapper,
    private val categoryMapper: CategoryMapper
) {

    /**
     * Maps Task With Category from Local to Repo.
     *
     * @param localTaskList the list of Task With Category to be converted.
     *
     * @return the converted list of Task With Category
     */
    fun toRepo(localTaskList: List<LocalTaskWithCategory>): List<RepoTaskWithCategory> =
        localTaskList.map { toRepo(it) }

    private fun toRepo(localTask: LocalTaskWithCategory): RepoTaskWithCategory =
        RepoTaskWithCategory(
            task = taskMapper.toRepo(localTask.task),
            category = localTask.category?.let { categoryMapper.toRepo(it) }
        )
}
