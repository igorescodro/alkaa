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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

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
        val flow = when (state) {
            is TaskListState.ShowAllTasks -> loadAllTasksUseCase()
            is TaskListState.ShowCompletedTasks -> loadAllCompletedTasksUseCase()
            is TaskListState.ShowTaskByCategory -> {
                categoryId = state.categoryId
                loadTasksByCategoryUseCase(state.categoryId)
            }
        }

        flow.map { taskWithCategoryMapper.toView(it) }
            .catch { handleException(it, onLoadError) }
            .collect {
                val shouldShow = state !is TaskListState.ShowCompletedTasks
                onTasksLoaded(it, shouldShow)
            }
    }

    private fun handleException(throwable: Throwable, onLoadError: () -> Unit) {
        Timber.d("handleException = $throwable")
        categoryId = 0L
        onLoadError()
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
}
