package com.escodro.task.presentation.list

import android.text.TextUtils
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.launch

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
    ) = viewModelScope.launch {
        val observable = when (state) {
            is TaskListState.ShowAllTasks -> loadAllTasksUseCase()
            is TaskListState.ShowCompletedTasks -> loadAllCompletedTasksUseCase()
            is TaskListState.ShowTaskByCategory -> {
                categoryId = state.categoryId
                loadTasksByCategoryUseCase(state.categoryId)
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

        viewModelScope.launch {
            val categoryIdValue = if (categoryId != 0L) categoryId else null
            val task = Task(title = description, categoryId = categoryIdValue)
            addTaskUseCase(taskMapper.toDomain(task))
        }
    }

    /**
     * Updates the task status as completed or uncompleted.
     *
     * @param task task to be updated
     */
    fun updateTaskStatus(task: Task) = viewModelScope.launch {
        updateStatusUseCase(task.id)
    }

    /**
     * Deletes the given task.
     *
     * @param taskWithCategory task to be removed
     */
    fun deleteTask(taskWithCategory: TaskWithCategory) = viewModelScope.launch {
        val task = taskWithCategory.task
        deleteTaskUseCase(taskMapper.toDomain(task))
    }

    /**
     * Clears the [ViewModel] when the [TaskListFragment] is not visible to user.
     */
    fun onDetach() {
        compositeDisposable.clear()
    }
}
