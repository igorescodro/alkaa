package com.escodro.task.presentation.detail.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escodro.core.coroutines.CoroutineDebouncer
import com.escodro.domain.usecase.task.LoadTask
import com.escodro.domain.usecase.task.UpdateTaskDescription
import com.escodro.domain.usecase.task.UpdateTaskTitle
import com.escodro.task.mapper.TaskMapper
import kotlinx.coroutines.launch

internal class TaskDetailViewModel(
    private val loadTaskUseCase: LoadTask,
    private val updateTaskTitle: UpdateTaskTitle,
    private val updateTaskDescription: UpdateTaskDescription,
    private val taskMapper: TaskMapper
) : ViewModel() {

    private val _state: MutableState<TaskDetailState> = mutableStateOf(TaskDetailState.Error)

    val state: State<TaskDetailState>
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
}
