package com.escodro.alkaa.ui.category.create

import com.escodro.alkaa.common.extension.applySchedulers
import com.escodro.alkaa.data.local.model.Category
import com.escodro.alkaa.di.DaoRepository
import io.reactivex.Observable

/**
 * Class containing the contract methods related to [NewCategoryViewModel].
 */
class NewCategoryContract(daoRepository: DaoRepository) {

    private val categoryDao = daoRepository.getCategoryDao()

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
