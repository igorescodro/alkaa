package com.escodro.domain.usecase.fake

import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class SearchRepositoryFake(
    taskRepository: TaskRepositoryFake,
    categoryRepository: CategoryRepositoryFake = CategoryRepositoryFake()
) : SearchRepository {

    private val taskWithCategoryRepository =
        TaskWithCategoryRepositoryFake(taskRepository, categoryRepository)

    override suspend fun findTaskByName(query: String): Flow<List<TaskWithCategory>> =
        taskWithCategoryRepository.findAllTasksWithCategory()
            .map { list: List<TaskWithCategory> ->
                list.filter { taskWithCategory ->
                    taskWithCategory.task.title.contains(query)
                }
            }
}
