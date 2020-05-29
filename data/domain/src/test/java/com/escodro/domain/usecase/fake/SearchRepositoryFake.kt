package com.escodro.domain.usecase.fake

import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.repository.SearchRepository
import kotlinx.coroutines.flow.first

internal class SearchRepositoryFake(
    taskRepository: TaskRepositoryFake,
    categoryRepository: CategoryRepositoryFake = CategoryRepositoryFake()
) : SearchRepository {

    private val taskWithCategoryRepository =
        TaskWithCategoryRepositoryFake(taskRepository, categoryRepository)

    override suspend fun findTaskByName(query: String): List<TaskWithCategory> =
        taskWithCategoryRepository.findAllTasksWithCategory()
            .first()
            .filter { taskWithCategory -> taskWithCategory.task.title.contains(query) }
}
