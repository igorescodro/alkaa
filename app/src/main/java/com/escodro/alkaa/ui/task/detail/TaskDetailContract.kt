package com.escodro.alkaa.ui.task.detail

import com.escodro.alkaa.common.extension.applySchedulers
import com.escodro.alkaa.data.local.model.Category
import com.escodro.alkaa.data.local.model.Task
import com.escodro.alkaa.di.DaoRepository
import io.reactivex.Flowable
import io.reactivex.Observable

/**
 * Class containing the contract methods related to [TaskDetailViewModel].
 */
class TaskDetailContract(daoRepository: DaoRepository) {

    private val categoryDao = daoRepository.getCategoryDao()

    private val taskDao = daoRepository.getTaskDao()

    /**
     * Loads all categories.
     *
     * @return a mutable list of all categories
     */
    fun loadAllCategories(): Flowable<MutableList<Category>> =
        categoryDao.getAllCategories().applySchedulers()

    /**
     * Updates the given task.
     *
     * @param task the task to be updated
     */
    fun updateTask(task: Task): Observable<Unit> =
        Observable.fromCallable { taskDao.updateTask(task) }.applySchedulers()
}
