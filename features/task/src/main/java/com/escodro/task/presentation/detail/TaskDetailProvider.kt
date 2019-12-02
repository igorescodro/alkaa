package com.escodro.task.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.escodro.core.extension.applySchedulers
import com.escodro.core.extension.notify
import com.escodro.domain.usecase.task.GetTask
import com.escodro.domain.usecase.task.UpdateTask
import com.escodro.task.mapper.TaskMapper
import com.escodro.task.model.Task
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

/**
 * Provides the [MutableLiveData] of [ViewData.Task] to be used in all screens that needs to share
 * the same information reactivity.
 */
internal class TaskDetailProvider(
    private val getTaskUseCase: GetTask,
    private val updateTaskUseCase: UpdateTask,
    private val taskMapper: TaskMapper
) {

    val taskData: LiveData<Task>
        get() = mutableTaskData

    private var mutableTaskData = MutableLiveData<Task>()

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
                    mutableTaskData.value = taskMapper.toView(it)
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

        val disposable = updateTaskUseCase(taskMapper.toDomain(task)).subscribe()

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
