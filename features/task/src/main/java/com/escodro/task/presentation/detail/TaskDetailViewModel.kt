package com.escodro.task.presentation.detail

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class TaskDetailViewModel : ViewModel() {

    private val _state: MutableStateFlow<TaskDetailState> = MutableStateFlow(TaskDetailState.Error)

    val state: StateFlow<TaskDetailState>
        get() = _state

    fun setTaskInfo(taskId: Long) {
        // _state.value = TaskDetailState.Loaded(taskWithCategory = taskWithCategory)
    }
}
