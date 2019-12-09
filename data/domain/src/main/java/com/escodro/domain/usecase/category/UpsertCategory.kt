package com.escodro.domain.usecase.category

import com.escodro.core.extension.applySchedulers
import com.escodro.domain.model.Category
import com.escodro.domain.repository.CategoryRepository
import io.reactivex.Completable

/**
 * Use case to save or update a category in the database.
 */
class UpsertCategory(private val categoryRepository: CategoryRepository) {

    /**
     * Adds or updates a category.
     *
     * @param category category to be added or updated
     *
     * @return observable to be subscribe
     */
    operator fun invoke(category: Category): Completable {
        val isNewCategory = category.id == 0L

        return if (isNewCategory) {
            categoryRepository.insertCategory(category).applySchedulers()
        } else {
            categoryRepository.updateCategory(category).applySchedulers()
        }
    }
}
