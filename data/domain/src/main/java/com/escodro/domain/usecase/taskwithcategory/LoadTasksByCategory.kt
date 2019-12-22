package com.escodro.domain.usecase.taskwithcategory

import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.repository.CategoryRepository
import com.escodro.domain.repository.TaskWithCategoryRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case to get a task with category by the category id from the database.
 */
class LoadTasksByCategory(
    private val joinRepository: TaskWithCategoryRepository,
    private val categoryRepository: CategoryRepository
) {

    /**
     * Gets a task with category by the category id if the category exists.
     *
     * @param categoryId the category id
     *
     * @return observable to be subscribe
     */
    suspend operator fun invoke(categoryId: Long): Flow<List<TaskWithCategory>> {
        val category = categoryRepository.findCategoryById(categoryId)
        return if (category != null) {
            joinRepository.findAllTasksWithCategoryId(category.id)
        } else {
            throw IllegalArgumentException("Category not found with id = $categoryId")
        }
    }
}
