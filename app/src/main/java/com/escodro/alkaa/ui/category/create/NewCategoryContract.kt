package com.escodro.alkaa.ui.category.create

import com.escodro.alkaa.common.extension.applySchedulers
import com.escodro.alkaa.data.local.model.Category
import com.escodro.alkaa.di.provider.DaoProvider
import io.reactivex.Observable

/**
 * Class containing the contract methods related to [NewCategoryViewModel].
 */
class NewCategoryContract(daoProvider: DaoProvider) {

    private val categoryDao = daoProvider.getCategoryDao()

    /**
     * Adds a category.
     *
     * @param category category to be added
     *
     * @return observable to be subscribe
     */
    fun addCategory(category: Category): Observable<Unit> =
        Observable.fromCallable { categoryDao.insertCategory(category) }.applySchedulers()
}
