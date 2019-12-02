package com.escodro.task.presentation.list

import android.text.TextUtils
import androidx.lifecycle.ViewModel
import com.escodro.domain.usecase.task.AddTask
import com.escodro.domain.usecase.task.DeleteTask
import com.escodro.domain.usecase.task.UpdateTaskStatus
import com.escodro.domain.usecase.taskwithcategory.LoadCompletedTasks
import com.escodro.domain.usecase.taskwithcategory.LoadTasksByCategory
import com.escodro.domain.usecase.taskwithcategory.LoadUncompletedTasks
import com.escodro.task.mapper.TaskMapper
import com.escodro.task.mapper.TaskWithCategoryMapper
import com.escodro.task.model.Task
import com.escodro.task.model.TaskWithCategory
import io.reactivex.disposables.CompositeDisposable

/**
 * [ViewModel] responsible to provide information to [TaskListFragment].
 */
internal class TaskListViewModel(
    private val loadAllTasksUseCase: LoadUncompletedTasks,
    private val loadAllCompletedTasksUseCase: LoadCompletedTasks,
    private val loadTasksByCategoryUseCase: LoadTasksByCategory,
    private val addTaskUseCase: AddTask,
    private val updateStatusUseCase: UpdateTaskStatus,
    private val deleteTaskUseCase: DeleteTask,
    private val taskMapper: TaskMapper,
    private val taskWithCategoryMapper: TaskWithCategoryMapper
) : ViewModel() {

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
        onTasksLoaded: (list: List<TaskWithCategory>, shouldShowAddButton: Boolean) -> Unit,
        onLoadError: () -> Unit
    ) {
        val observable = when (state) {
            is TaskListState.ShowAllTasks -> loadAllTasksUseCase.test()
            is TaskListState.ShowCompletedTasks -> loadAllCompletedTasksUseCase.test()
            is TaskListState.ShowTaskByCategory -> {
                categoryId = state.categoryId
                loadTasksByCategoryUseCase.test(state.categoryId)
            }
        }

        val disposable = observable
            .map { taskWithCategoryMapper.toView(it) }
            .subscribe(
                {
                    val shouldShow = state !is TaskListState.ShowCompletedTasks
                    onTasksLoaded(it, shouldShow)
                },
                {
                    categoryId = 0L
                    onLoadError()
                })

        compositeDisposable.add(disposable)
    }

    /**
     * Add a new task.
     */
    fun addTask(description: String) {
        if (TextUtils.isEmpty(description)) return

        val categoryIdValue = if (categoryId != 0L) categoryId else null
        val task = Task(title = description, categoryId = categoryIdValue)
        val disposable = addTaskUseCase.test(taskMapper.toDomain(task)).subscribe()
        compositeDisposable.add(disposable)
    }

    /**
     * Updates the task status as completed or uncompleted.
     *
     * @param task task to be updated
     */
    fun updateTaskStatus(task: Task) {
        val disposable = updateStatusUseCase.test(taskMapper.toDomain(task)).subscribe()
        compositeDisposable.add(disposable)
    }

    /**
     * Deletes the given task.
     *
     * @param taskWithCategory task to be removed
     */
    fun deleteTask(taskWithCategory: TaskWithCategory) {
        val task = taskWithCategory.task
        val disposable = deleteTaskUseCase.test(taskMapper.toDomain(task)).subscribe()
        compositeDisposable.add(disposable)
    }

    /**
     * Clears the [ViewModel] when the [TaskListFragment] is not visible to user.
     */
    fun onDetach() {
        compositeDisposable.clear()
    }
}
