package com.escodro.alkaa.ui.task.detail

import com.escodro.alkaa.common.extension.applySchedulers
import com.escodro.alkaa.data.local.model.Task
import com.escodro.alkaa.di.provider.DaoProvider
import io.reactivex.Observable

/**
 * Class containing the contract methods related to [TaskDetailViewModel].
 */
class TaskDetailContract(daoProvider: DaoProvider) {

    private val taskDao = daoProvider.getTaskDao()

    /**
     * Updates the given task.
     *
     * @param task the task to be updated
     */
    fun updateTask(task: Task): Observable<Unit> =
        Observable.fromCallable { taskDao.updateTask(task) }.applySchedulers()
}
