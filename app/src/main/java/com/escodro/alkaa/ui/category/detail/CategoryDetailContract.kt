package com.escodro.alkaa.ui.category.detail

import com.escodro.alkaa.common.extension.applySchedulers
import com.escodro.alkaa.data.local.model.Category
import com.escodro.alkaa.di.provider.DaoProvider
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Class containing the contract methods related to [CategoryDetailViewModel].
 */
class CategoryDetailContract(daoProvider: DaoProvider) {

    private val categoryDao = daoProvider.getCategoryDao()

    /**
     * Loads the category based on the given id.
     *
     * @param categoryId category id
     */
    fun loadCategory(categoryId: Long): Single<Category> =
        categoryDao.findCategory(categoryId).applySchedulers()

    /**
     * Adds or updates a category.
     *
     * @param category category to be added or updated
     *
     * @return observable to be subscribe
     */
    fun saveCategory(category: Category): Observable<Unit> {
        val isNewCategory = category.id == 0L

        return if (isNewCategory) {
            Observable.fromCallable { categoryDao.insertCategory(category) }.applySchedulers()
        } else {
            Observable.fromCallable { categoryDao.updateCategory(category) }.applySchedulers()
        }
    }
}
