package com.escodro.alkaa.ui.task.list

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.text.TextUtils
import com.escodro.alkaa.data.local.model.Task
import com.escodro.alkaa.data.local.model.TaskWithCategory
import io.reactivex.disposables.CompositeDisposable

/**
 * [ViewModel] responsible to provide information to [TaskListFragment].
 */
class TaskListViewModel(private val contract: TaskListContract) : ViewModel() {

    var delegate: TaskListDelegate? = null

    val newTask = MutableLiveData<String>()

    private var categoryId: Long? = null

    private val compositeDisposable = CompositeDisposable()

    /**
     * Loads all tasks.
     *
     * @param categoryId the category id to show only tasks related to this category, if `0` is
     * passed, all the categories will be shown.
     */
    fun loadTasks(categoryId: Long) {
        this.categoryId = categoryId

        compositeDisposable.clear()
        compositeDisposable.add(
            contract.loadTasks(categoryId).subscribe { delegate?.updateList(it) })
    }

    /**
     * Add a new task.
     */
    fun addTask() {
        val description = newTask.value
        if (TextUtils.isEmpty(description)) {
            delegate?.onEmptyField()
            return
        }

        val categoryIdValue = if (categoryId != 0L) categoryId else null
        val task = Task(description = description, categoryId = categoryIdValue)
        val taskWithCategory = TaskWithCategory(task)
        contract.addTask(task)
            ?.doOnComplete { onNewTaskAdded(taskWithCategory) }
            ?.subscribe()
    }

    /**
     * Updates the task status.
     *
     * @param task task to be updated
     */
    fun updateTaskStatus(task: Task, isCompleted: Boolean) {
        task.completed = isCompleted
        contract.updateTask(task).subscribe()
    }

    /**
     * Deletes the given task.
     *
     * @param taskWithCategory task to be removed
     */
    fun deleteTask(taskWithCategory: TaskWithCategory) {
        contract.deleteTask(taskWithCategory.task)
            .doOnComplete { onTaskRemoved(taskWithCategory) }
            .subscribe()
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    private fun onNewTaskAdded(taskWithCategory: TaskWithCategory) {
        newTask.value = null
        delegate?.onNewTaskAdded(taskWithCategory)
    }

    private fun onTaskRemoved(taskWithCategory: TaskWithCategory) {
        delegate?.onTaskRemoved(taskWithCategory)
    }
}
