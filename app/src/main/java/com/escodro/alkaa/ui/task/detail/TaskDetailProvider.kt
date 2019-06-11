package com.escodro.alkaa.ui.task.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.escodro.alkaa.data.local.model.Task
import com.escodro.core.extension.applySchedulers
import com.escodro.core.extension.notify
import com.escodro.domain.usecase.task.GetTask
import com.escodro.domain.usecase.task.UpdateTask
import com.escodro.domain.viewdata.ViewData
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

/**
 * Provides the [MutableLiveData] of [Task] to be used in [TaskDetailFragment] layout.
 */
class TaskDetailProvider(
    private val getTaskUseCase: GetTask,
    private val updateTaskUseCase: UpdateTask
) {

    val taskData: LiveData<ViewData.Task>
        get() = mutableTaskData

    private var mutableTaskData = MutableLiveData<ViewData.Task>()

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

        val disposable = getTaskUseCase(taskId)
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
    fun updateTask(task: ViewData.Task) {
        Timber.d("updateTask() - $task")

        val disposable = Observable.fromCallable { updateTaskUseCase(task) }
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
