package com.escodro.alkaa.ui.main

import com.escodro.alkaa.common.extension.applySchedulers
import com.escodro.alkaa.data.local.model.Category
import com.escodro.alkaa.di.provider.DaoProvider
import io.reactivex.Flowable

/**
 * Class containing the contract methods related to [MainActivity].
 */
class MainContract(daoProvider: DaoProvider) {

    private val categoryDao = daoProvider.getCategoryDao()

    /**
     * Loads all categories.
     *
     * @return a mutable list of all categories
     */
    fun loadCategories(): Flowable<MutableList<Category>> =
        categoryDao.getAllCategories().applySchedulers()
}
