package com.escodro.alkaa.ui.category.create

import com.escodro.alkaa.data.local.model.Category
import com.escodro.alkaa.di.DaoRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Class containing the contract methods related to [NewCategoryViewModel].
 *
 * @author Igor Escodro on 3/6/18.
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
    fun addCategory(category: Category): Observable<Unit>? =
        Observable.fromCallable { categoryDao.insertCategory(category) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}
