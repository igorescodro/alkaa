package com.escodro.alkaa.ui.task.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.escodro.core.extension.applySchedulers
import com.escodro.core.extension.notify
import com.escodro.alkaa.data.local.model.Task
import com.escodro.alkaa.di.provider.DaoProvider
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

/**
 * Provides the [MutableLiveData] of [Task] to be used in [TaskDetailFragment] layout.
 */
class TaskDetailProvider(daoProvider: DaoProvider) {

    val taskData: LiveData<Task>
        get() = mutableTaskData

    private var mutableTaskData = MutableLiveData<Task>()

    private val taskDao = daoProvider.getTaskDao()

    private val compositeDisposable = CompositeDisposable()

    /**
     * Loads the task based on the given id.
     *
     * @param taskId task id
     */
    fun loadTask(taskId: Long?) {
        if (taskId == null) {
            Timber.e("loadTask - Task id is null")
            return
        }

        val disposable = taskDao.getTaskById(taskId)
            .applySchedulers().subscribe(
                {
                    Timber.d("loadTask = ${it.title}")
                    mutableTaskData.value = it
                },
                { Timber.e("Task not found in database") })

        compositeDisposable.add(disposable)
    }

    /**
     * Updates the given task in database.
     *
     * @param task task to be updated
     */
    fun updateTask(task: Task) {
        Timber.d("updateTask() - $task")

        val disposable = Observable.fromCallable { taskDao.updateTask(task) }
            .applySchedulers()
            .subscribe()

        mutableTaskData.notify()
        compositeDisposable.add(disposable)
    }

    /**
     * Clears the provider.
     */
    fun clear() {
        Timber.d("clear()")
        mutableTaskData = MutableLiveData()
        compositeDisposable.clear()
    }
}
