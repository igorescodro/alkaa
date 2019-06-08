package com.escodro.domain.usecase

import com.escodro.domain.extension.applySchedulers
import com.escodro.domain.mapper.CategoryMapper
import com.escodro.domain.viewdata.ViewData
import com.escodro.local.provider.DaoProvider
import io.reactivex.Flowable

/**
 * Use case to load all categories from the database.
 */
class LoadCategories(private val daoProvider: DaoProvider, private val mapper: CategoryMapper) {

    /**
     * Loads all categories.
     *
     * @return a mutable list of all categories
     */
    operator fun invoke(): Flowable<List<ViewData.Category>> =
        daoProvider
            .getCategoryDao()
            .getAllCategories()
            .map { mapper.toViewCategory(it) }
            .applySchedulers()
}
