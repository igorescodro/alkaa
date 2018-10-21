package com.escodro.alkaa.ui.category.list

import com.escodro.alkaa.common.extension.applySchedulers
import com.escodro.alkaa.data.local.model.Category
import com.escodro.alkaa.di.provider.DaoProvider
import io.reactivex.Flowable
import io.reactivex.Observable

/**
 * Class containing the contract methods related to [CategoryListViewModel].
 */
class CategoryListContract(daoProvider: DaoProvider) {

    private val categoryDao = daoProvider.getCategoryDao()

    /**
     * Loads all categories.
     *
     * @return a mutable list of all categories
     */
    fun loadCategories(): Flowable<MutableList<Category>> =
        categoryDao.getAllCategories().applySchedulers()

    /**
     * Deletes a category.
     *
     * @param category category to be deleted
     *
     * @return observable to be subscribe
     */
    fun deleteTask(category: Category): Observable<Unit> =
        Observable.fromCallable { categoryDao.deleteCategory(category) }.applySchedulers()
}
