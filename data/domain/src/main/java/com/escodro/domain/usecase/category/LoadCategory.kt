package com.escodro.domain.usecase.category

import com.escodro.domain.model.Category
import com.escodro.domain.repository.CategoryRepository
import javax.inject.Inject

/**
 * Use case to load a specific category from the database.
 */
class LoadCategory @Inject constructor(private val categoryRepository: CategoryRepository) {

    /**
     * Loads the category based on the given id.
     *
     * @param categoryId category id
     *
     * @return an single observable to be subscribed
     */
    suspend operator fun invoke(categoryId: Long): Category? =
        categoryRepository.findCategoryById(categoryId)
}
