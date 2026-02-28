package com.escodro.task.presentation.detail.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escodro.coroutines.AppCoroutineScope
import com.escodro.coroutines.CoroutineDebouncer
import com.escodro.domain.usecase.task.LoadTaskFlow
import com.escodro.domain.usecase.task.UpdateTaskCategory
import com.escodro.domain.usecase.task.UpdateTaskDescription
import com.escodro.domain.usecase.task.UpdateTaskStatus
import com.escodro.domain.usecase.task.UpdateTaskTitle
import com.escodro.task.mapper.TaskMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Suppress("LongParameterList")
internal class TaskDetailViewModel(
    private val loadTaskUseCase: LoadTaskFlow,
    private val updateTaskTitle: UpdateTaskTitle,
    private val updateTaskDescription: UpdateTaskDescription,
    private val updateTaskCategory: UpdateTaskCategory,
    private val updateTaskStatus: UpdateTaskStatus,
    private val coroutineDebouncer: CoroutineDebouncer,
    private val applicationScope: AppCoroutineScope,
    private val taskMapper: TaskMapper,
) : ViewModel() {

    fun loadTaskInfo(taskId: TaskId): Flow<TaskDetailState> =
        loadTaskUseCase(taskId = taskId.value).map { task ->
            if (task != null) {
                val viewTask = taskMapper.toView(task)
                TaskDetailState.Loaded(viewTask)
            } else {
                TaskDetailState.Error
            }
        }

    fun updateTitle(taskId: TaskId, title: String) {
        coroutineDebouncer(coroutineScope = viewModelScope) {
            updateTaskTitle(taskId.value, title)
        }
    }

    fun updateDescription(taskId: TaskId, description: String) {
        coroutineDebouncer(coroutineScope = viewModelScope) {
            updateTaskDescription(taskId.value, description)
        }
    }

    fun updateCategory(taskId: TaskId, categoryId: CategoryId) =
        applicationScope.launch {
            updateTaskCategory(taskId = taskId.value, categoryId = categoryId.value)
        }

    fun updateTaskStatus(taskId: TaskId) =
        applicationScope.launch {
            updateTaskStatus(taskId = taskId.value)
        }
}
