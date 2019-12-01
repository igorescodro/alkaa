package com.escodro.domain.usecase.category

import com.escodro.core.extension.applySchedulers
import com.escodro.domain.mapper.CategoryMapper
import com.escodro.domain.model.Category
import com.escodro.domain.repository.CategoryRepository
import com.escodro.domain.viewdata.ViewData
import com.escodro.local.provider.DaoProvider
import io.reactivex.Flowable

/**
 * Use case to load all categories from the database.
 */
class LoadAllCategories(
    private val categoryRepository: CategoryRepository,
    private val daoProvider: DaoProvider,
    private val mapper: CategoryMapper
) {

    /**
     * Loads all categories.
     *
     * @return a mutable list of all categories
     */
    operator fun invoke(): Flowable<List<ViewData.Category>> =
        daoProvider.getCategoryDao()
            .findAllCategories()
            .map { mapper.toViewCategory(it) }
            .applySchedulers()

    @Suppress("UndocumentedPublicFunction")
    fun test(): Flowable<List<Category>> =
        categoryRepository.findAllCategories().applySchedulers()
}
