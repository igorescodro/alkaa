package com.escodro.domain.usecase.category

import com.escodro.core.extension.applySchedulers
import com.escodro.domain.mapper.CategoryMapper
import com.escodro.domain.model.Category
import com.escodro.domain.repository.CategoryRepository
import com.escodro.domain.viewdata.ViewData
import com.escodro.local.provider.DaoProvider
import io.reactivex.Completable

/**
 * Use case to save or update a category in the database.
 */
class SaveCategory(
    private val categoryRepository: CategoryRepository,
    daoProvider: DaoProvider,
    private val mapper: CategoryMapper
) {

    private val categoryDao = daoProvider.getCategoryDao()

    /**
     * Adds or updates a category.
     *
     * @param category category to be added or updated
     *
     * @return observable to be subscribe
     */
    operator fun invoke(category: ViewData.Category): Completable {
        val categoryEntity = mapper.toEntityCategory(category)
        val isNewCategory = categoryEntity.id == 0L

        return if (isNewCategory) {
            categoryDao.insertCategory(categoryEntity).applySchedulers()
        } else {
            categoryDao.updateCategory(categoryEntity).applySchedulers()
        }
    }

    @Suppress("UndocumentedPublicFunction")
    fun test(category: Category): Completable {
        val isNewCategory = category.id == 0L

        return if (isNewCategory) {
            categoryRepository.insertCategory(category).applySchedulers()
        } else {
            categoryRepository.updateCategory(category).applySchedulers()
        }
    }
}
