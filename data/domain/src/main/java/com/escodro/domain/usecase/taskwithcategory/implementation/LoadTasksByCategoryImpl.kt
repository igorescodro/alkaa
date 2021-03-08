package com.escodro.domain.usecase.taskwithcategory.implementation

import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.repository.CategoryRepository
import com.escodro.domain.repository.TaskWithCategoryRepository
import com.escodro.domain.usecase.taskwithcategory.LoadTasksByCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class LoadTasksByCategoryImpl(
    private val joinRepository: TaskWithCategoryRepository,
    private val categoryRepository: CategoryRepository
) : LoadTasksByCategory {

    override suspend operator fun invoke(categoryId: Long): Flow<List<TaskWithCategory>> {
        val category = categoryRepository.findCategoryById(categoryId)
        return if (category != null) {
            joinRepository.findAllTasksWithCategoryId(category.id)
        } else {
            flow { throw IllegalArgumentException("Category not found with id = $categoryId") }
        }
    }
}
