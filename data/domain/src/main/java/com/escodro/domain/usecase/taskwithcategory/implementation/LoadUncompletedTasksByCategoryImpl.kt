package com.escodro.domain.usecase.taskwithcategory.implementation

import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.repository.CategoryRepository
import com.escodro.domain.repository.TaskWithCategoryRepository
import com.escodro.domain.usecase.taskwithcategory.LoadUncompletedTasksByCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

internal class LoadUncompletedTasksByCategoryImpl(
    private val joinRepository: TaskWithCategoryRepository,
    private val categoryRepository: CategoryRepository
) : LoadUncompletedTasksByCategory {

    override suspend operator fun invoke(categoryId: Long): Flow<List<TaskWithCategory>> {
        val category = categoryRepository.findCategoryById(categoryId)
        return if (category != null) {
            joinRepository.findAllTasksWithCategoryId(category.id)
                .map { list -> list.filterNot { item -> item.task.completed } }
        } else {
            flow { throw IllegalArgumentException("Category not found with id = $categoryId") }
        }
    }
}
