package com.escodro.task.presentation.detail.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escodro.core.coroutines.CoroutineDebouncer
import com.escodro.domain.usecase.task.LoadTask
import com.escodro.domain.usecase.task.UpdateTaskCategory
import com.escodro.domain.usecase.task.UpdateTaskDescription
import com.escodro.domain.usecase.task.UpdateTaskTitle
import com.escodro.task.mapper.TaskMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

internal class TaskDetailViewModel(
    private val loadTaskUseCase: LoadTask,
    private val updateTaskTitle: UpdateTaskTitle,
    private val updateTaskDescription: UpdateTaskDescription,
    private val updateTaskCategory: UpdateTaskCategory,
    private val taskMapper: TaskMapper
) : ViewModel() {

    private val coroutineDebouncer = CoroutineDebouncer()

    fun loadTaskInfo(taskId: TaskId): Flow<TaskDetailState> = flow {
        val task = loadTaskUseCase(taskId = taskId.value)

        if (task != null) {
            val viewTask = taskMapper.toView(task)
            emit(TaskDetailState.Loaded(viewTask))
        } else {
            emit(TaskDetailState.Error)
        }
    }

    fun updateTitle(taskId: TaskId, title: String) =
        coroutineDebouncer(coroutineScope = viewModelScope) {
            updateTaskTitle(taskId.value, title)
        }

    fun updateDescription(taskId: TaskId, description: String) =
        coroutineDebouncer(coroutineScope = viewModelScope) {
            updateTaskDescription(taskId.value, description)
        }

    fun updateCategory(taskId: TaskId, categoryId: CategoryId) =
        viewModelScope.launch {
            updateTaskCategory(taskId = taskId.value, categoryId = categoryId.value)
        }
}
