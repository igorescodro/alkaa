package com.escodro.repository.mapper

import com.escodro.domain.model.TaskWithCategory as DomainTaskWithCategory
import com.escodro.repository.model.TaskWithCategory as RepoTaskWithCategory

/**
 * Maps Task With Category between Repository and Domain.
 */
internal class TaskWithCategoryMapper(
    private val taskMapper: TaskMapper,
    private val categoryMapper: CategoryMapper
) {

    /**
     * Maps Task With Category from Domain to Repo.
     *
     * @param localTaskList the list of Task With Category to be converted.
     *
     * @return the converted list of Task With Category
     */
    fun toDomain(localTaskList: List<RepoTaskWithCategory>): List<DomainTaskWithCategory> =
        localTaskList.map { toDomain(it) }

    private fun toDomain(localTask: RepoTaskWithCategory): DomainTaskWithCategory =
        DomainTaskWithCategory(
            task = taskMapper.toDomain(localTask.task),
            category = localTask.category?.let { categoryMapper.toDomain(it) }
        )
}
