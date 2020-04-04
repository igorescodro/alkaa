package com.escodro.task.presentation.detail.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escodro.domain.usecase.task.UpdateTask
import com.escodro.task.mapper.TaskMapper
import com.escodro.task.model.Task
import com.escodro.task.presentation.detail.TaskDetailProvider
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * [ViewModel] responsible to provide information to [TaskDetailFragment].
 */
internal class TaskDetailViewModel(
    private val taskProvider: TaskDetailProvider,
    private val updateTaskUseCase: UpdateTask,
    private val taskMapper: TaskMapper
) : ViewModel() {

    val state: LiveData<TaskDetailUIState> =
        Transformations.map(taskProvider.taskData) { mapToUIState(it) }

    val taskData: LiveData<Task> = taskProvider.taskData

    /**
     * Loads the Task to be handled by the [ViewModel]s.
     *
     * @param id the task id
     */
    fun loadTask(id: Long) {
        Timber.d("loadTask() - $id")
        taskProvider.loadTask(id, viewModelScope)
    }

    /**
     * Updates the task title.
     *
     * @param title the task title
     */
    fun updateTitle(title: String) {
        Timber.d("updateTitle() - $title")

        if (title.isEmpty()) {
            return
        }

        viewModelScope.launch {
            taskData.value?.copy(title = title)?.let { task ->
                updateTaskUseCase(taskMapper.toDomain(task))
            }
        }
    }

    /**
     * Updates the task description.
     *
     * @param description the task description
     */
    fun updateDescription(description: String) {
        Timber.d("updateDescription() - $description")

        viewModelScope.launch {
            taskData.value?.copy(description = description)?.let { task ->
                updateTaskUseCase(taskMapper.toDomain(task))
            }
        }
    }

    private fun mapToUIState(task: Task): TaskDetailUIState =
        if (task.completed) {
            TaskDetailUIState.Completed
        } else {
            TaskDetailUIState.Uncompleted
        }

    /**
     * Clears the [ViewModel] when the [androidx.fragment.app.Fragment] is not visible to user.
     */
    fun onDetach() {
        taskProvider.clear()
    }
}
