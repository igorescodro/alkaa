package com.escodro.task.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escodro.core.coroutines.CoroutineDebouncer
import com.escodro.domain.usecase.task.LoadTask
import com.escodro.domain.usecase.task.UpdateTaskDescription
import com.escodro.domain.usecase.task.UpdateTaskTitle
import com.escodro.task.mapper.TaskMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class TaskDetailViewModel(
    private val loadTaskUseCase: LoadTask,
    private val updateTaskTitle: UpdateTaskTitle,
    private val updateTaskDescription: UpdateTaskDescription,
    private val taskMapper: TaskMapper
) : ViewModel() {

    private val _state: MutableStateFlow<TaskDetailState> = MutableStateFlow(TaskDetailState.Error)

    val state: StateFlow<TaskDetailState>
        get() = _state

    private val coroutineDebouncer = CoroutineDebouncer()

    fun setTaskInfo(taskId: Long) = viewModelScope.launch {
        val task = loadTaskUseCase(taskId = taskId)

        if (task != null) {
            val viewTask = taskMapper.toView(task)
            _state.value = TaskDetailState.Loaded(viewTask)
        } else {
            _state.value = TaskDetailState.Error
        }
    }

    fun updateTitle(title: String) {
        coroutineDebouncer(coroutineScope = viewModelScope) {
            _state.value.run {
                if (this is TaskDetailState.Loaded) {
                    updateTaskTitle(this.task.id, title)
                }
            }
        }
    }

    fun updateDescription(description: String) {
        coroutineDebouncer(coroutineScope = viewModelScope) {
            _state.value.run {
                if (this is TaskDetailState.Loaded) {
                    updateTaskDescription(this.task.id, description)
                }
            }
        }
    }

    // fun updateCategory(categoryId: Long?) = viewModelScope.launch {
    //     _state.value.run {
    //         if (this is TaskDetailState.Loaded) {
    //             val updatedTask = this.task.copy(categoryId = categoryId)
    //             updateTask(updatedTask)
    //         }
    //     }
    // }
    //
    // private suspend fun updateTask(task: Task) {
    //     val mappedTask = taskMapper.toDomain(task)
    //     updateTaskUseCase(mappedTask)
    // }
}
