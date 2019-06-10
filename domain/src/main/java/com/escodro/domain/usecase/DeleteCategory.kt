package com.escodro.domain.usecase

import com.escodro.domain.extension.applySchedulers
import com.escodro.domain.mapper.CategoryMapper
import com.escodro.domain.viewdata.ViewData
import com.escodro.local.provider.DaoProvider
import io.reactivex.Observable

/**
 * Use case to delete a category from the database.
 */
class DeleteCategory(private val daoProvider: DaoProvider, private val mapper: CategoryMapper) {

    /**
     * Deletes a category.
     *
     * @param category category to be deleted
     *
     * @return observable to be subscribe
     */
    operator fun invoke(category: ViewData.Category) =
        Observable.fromCallable {
            daoProvider
                .getCategoryDao()
                .deleteCategory(mapper.toEntityCategory(category))
        }.applySchedulers()
}
