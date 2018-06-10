package com.escodro.alkaa.ui.task.detail

import com.escodro.alkaa.data.local.model.Category
import com.escodro.alkaa.di.DaoRepository
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Class containing the contract methods related to [TaskDetailViewModel].
 *
 * @author Igor Escodro on 1/4/18.
 */
class TaskDetailContract(daoRepository: DaoRepository) {

    private val categoryDao = daoRepository.getCategoryDao()

    /**
     * Loads all categories.
     *
     * @return a mutable list of all categories
     */
    fun loadAllCategories(): Flowable<MutableList<Category>> =
        categoryDao.getAllCategories()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}
