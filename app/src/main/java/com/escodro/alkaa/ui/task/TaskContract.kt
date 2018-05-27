package com.escodro.alkaa.ui.task

import com.escodro.alkaa.data.local.model.Task
import com.escodro.alkaa.di.DaoRepository
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Class containing the contract methods related to [TaskViewModel].
 *
 * @author Igor Escodro on 1/4/18.
 */
class TaskContract(daoRepository: DaoRepository) {

    private val taskDao = daoRepository.getTaskDao()

    /**
     * Loads all tasks.
     *
     * @return a mutable list of all tasks
     */
    fun loadTasks(): Flowable<MutableList<Task>> =
        taskDao.getAllTasks()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    /**
     * Adds a task.
     *
     * @param task task to be added
     *
     * @return observable to be subscribe
     */
    fun addTask(task: Task): Observable<Unit>? =
        Observable.fromCallable { taskDao.insertTask(task) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    /**
     * Updates a task.
     *
     * @param task task to be added
     *
     * @return observable to be subscribed
     */
    fun updateTask(task: Task): Observable<Unit> =
        Observable.fromCallable { taskDao.updateTask(task) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    /**
     * Deletes a task.
     *
     * @param task task to be deleted
     *
     * @return observable to be subscribe
     */
    fun deleteTask(task: Task): Observable<Unit> =
        Observable.fromCallable { taskDao.deleteTask(task) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}
