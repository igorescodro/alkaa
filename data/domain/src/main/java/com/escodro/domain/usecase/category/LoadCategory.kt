package com.escodro.domain.usecase.category

import com.escodro.core.extension.applySchedulers
import com.escodro.domain.model.Category
import com.escodro.domain.repository.CategoryRepository
import io.reactivex.Single

/**
 * Use case to load a specific category from the database.
 */
class LoadCategory(private val categoryRepository: CategoryRepository) {

    /**
     * Loads the category based on the given id.
     *
     * @param categoryId category id
     *
     * @return an single observable to be subscribed
     */
    operator fun invoke(categoryId: Long): Single<Category> =
        categoryRepository.findCategoryById(categoryId).applySchedulers()
}
