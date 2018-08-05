package com.escodro.alkaa.ui.task.list

import com.escodro.alkaa.common.extension.applySchedulers
import com.escodro.alkaa.data.local.model.Task
import com.escodro.alkaa.data.local.model.TaskWithCategory
import com.escodro.alkaa.di.DaoRepository
import io.reactivex.Flowable
import io.reactivex.Observable

/**
 * Class containing the contract methods related to [TaskListViewModel].
 */
class TaskListContract(daoRepository: DaoRepository) {

    private val taskDao = daoRepository.getTaskDao()

    /**
     * Loads all tasks.
     *
     * @param categoryId the category id to show only tasks related to this category, if `0` is
     * passed, all the categories will be shown.
     *
     * @return a mutable list of all tasks
     */
    fun loadTasks(categoryId: Long): Flowable<MutableList<TaskWithCategory>> {
        val observable =
            if (categoryId == NO_CATEGORY) {
                taskDao.getAllTasksWithCategory()
            } else {
                taskDao.getAllTasksWithCategoryId(categoryId)
            }

        return observable.applySchedulers()
    }

    /**
     * Adds a task.
     *
     * @param task task to be added
     *
     * @return observable to be subscribe
     */
    fun addTask(task: Task): Observable<Unit> =
        Observable.fromCallable { taskDao.insertTask(task) }.applySchedulers()

    /**
     * Updates a task.
     *
     * @param task task to be added
     *
     * @return observable to be subscribed
     */
    fun updateTask(task: Task): Observable<Unit> =
        Observable.fromCallable { taskDao.updateTask(task) }.applySchedulers()

    /**
     * Deletes a task.
     *
     * @param task task to be deleted
     *
     * @return observable to be subscribe
     */
    fun deleteTask(task: Task): Observable<Unit> =
        Observable.fromCallable { taskDao.deleteTask(task) }.applySchedulers()

    companion object {

        private const val NO_CATEGORY = 0L
    }
}
