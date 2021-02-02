package com.escodro.task.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escodro.core.coroutines.CoroutineDebouncer
import com.escodro.domain.usecase.task.GetTask
import com.escodro.domain.usecase.task.UpdateTask
import com.escodro.task.mapper.TaskMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal class TaskDetailViewModel(
    private val getTaskUseCase: GetTask,
    private val updateTaskUseCase: UpdateTask,
    private val taskMapper: TaskMapper
) : ViewModel() {

    private val _state: MutableStateFlow<TaskDetailState> = MutableStateFlow(TaskDetailState.Error)

    val state: StateFlow<TaskDetailState>
        get() = _state

    private val coroutineDebouncer = CoroutineDebouncer()

    fun setTaskInfo(taskId: Long) = viewModelScope.launch {
        getTaskUseCase(taskId = taskId).collect { task ->
            val viewTask = taskMapper.toView(task)
            _state.value = TaskDetailState.Loaded(viewTask)
        }
    }

    fun updateTitle(title: String) {
        coroutineDebouncer(coroutineScope = viewModelScope) {
            _state.value.run {
                if (this is TaskDetailState.Loaded) {
                    val updatedTask = this.task.copy(title = title)
                    val mappedTask = taskMapper.toDomain(updatedTask)
                    updateTaskUseCase(mappedTask)
                }
            }
        }
    }
}
