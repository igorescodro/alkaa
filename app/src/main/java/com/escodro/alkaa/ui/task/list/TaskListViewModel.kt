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
     * Load the tasks based on the given state.
     *
     * @param state the list state
     * @param onTasksLoaded HOF to be called when the task list is loaded
     */
    fun loadTasks(
        state: TaskListState,
        onTasksLoaded: (list: List<TaskWithCategory>, shouldShowAddButton: Boolean) -> Unit
    ) {
        val observable = when (state) {
            is TaskListState.ShowAllTasks -> contract.loadAllTasks()
            is TaskListState.ShowCompletedTasks -> contract.loadAllCompletedTasks()
            is TaskListState.ShowTaskByCategory -> {
                categoryId = state.categoryId
                contract.loadTaskWithCategoryId(state.categoryId)
            }
        }

        val disposable = observable.subscribe {
            val shouldShow = state !is TaskListState.ShowCompletedTasks
            onTasksLoaded(it, shouldShow)
        }
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
