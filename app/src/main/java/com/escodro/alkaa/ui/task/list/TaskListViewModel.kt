package com.escodro.alkaa.ui.task.list

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.escodro.alkaa.data.local.model.Task
import com.escodro.alkaa.data.local.model.TaskWithCategory
import io.reactivex.disposables.CompositeDisposable

/**
 * [ViewModel] responsible to provide information to [TaskListFragment].
 */
class TaskListViewModel(private val contract: TaskListContract) : ViewModel() {

    val newTask = MutableLiveData<String>()

    private var categoryId: Long? = null

    private val compositeDisposable = CompositeDisposable()

    /**
     * Loads all tasks.
     *
     * @param categoryId the category id to show only tasks related to this category, if `0` is
     * passed, all the categories will be shown.
     */
    fun loadTasks(categoryId: Long, onTasksLoaded: (list: List<TaskWithCategory>) -> Unit) {
        this.categoryId = categoryId

        val disposable = contract.loadTasks(categoryId).subscribe { onTasksLoaded(it) }
        compositeDisposable.add(disposable)
    }

    /**
     * Add a new task.
     */
    fun addTask(onEmptyField: () -> Unit) {
        val description = newTask.value
        if (TextUtils.isEmpty(description)) {
            onEmptyField()
            return
        }

        val categoryIdValue = if (categoryId != 0L) categoryId else null
        val task = Task(description = description, categoryId = categoryIdValue)
        val disposable = contract.addTask(task)
            .doOnComplete { newTask.value = null }
            .subscribe()
        compositeDisposable.add(disposable)
    }

    /**
     * Updates the task status.
     *
     * @param task task to be updated
     */
    fun updateTaskStatus(task: Task, isCompleted: Boolean) {
        task.completed = isCompleted

        val disposable = contract.updateTask(task).subscribe()
        compositeDisposable.add(disposable)
    }

    /**
     * Deletes the given task.
     *
     * @param taskWithCategory task to be removed
     */
    fun deleteTask(taskWithCategory: TaskWithCategory) {
        val disposable = contract.deleteTask(taskWithCategory.task)
            .subscribe()
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }
}
