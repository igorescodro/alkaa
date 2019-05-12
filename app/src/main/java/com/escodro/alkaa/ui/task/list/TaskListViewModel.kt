package com.escodro.alkaa.ui.task.list

import android.text.TextUtils
import androidx.lifecycle.ViewModel
import com.escodro.alkaa.data.local.model.Task
import com.escodro.alkaa.data.local.model.TaskWithCategory
import io.reactivex.disposables.CompositeDisposable

/**
 * [ViewModel] responsible to provide information to [TaskListFragment].
 */
class TaskListViewModel(private val contract: TaskListContract) : ViewModel() {

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
    fun addTask(description: String) {
        if (TextUtils.isEmpty(description)) return

        val categoryIdValue = if (categoryId != 0L) categoryId else null
        val task = Task(title = description, categoryId = categoryIdValue)
        val disposable = contract.addTask(task)
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
        val task = taskWithCategory.task
        val disposable = contract.deleteTask(task)
            .subscribe()
        compositeDisposable.add(disposable)
    }

    /**
     * Clears the [ViewModel] when the [TaskListFragment] is not visible to user.
     */
    fun onDetach() {
        compositeDisposable.clear()
    }
}
