package com.escodro.task.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escodro.core.coroutines.CoroutineDebouncer
import com.escodro.domain.usecase.category.LoadAllCategories
import com.escodro.domain.usecase.task.LoadTask
import com.escodro.domain.usecase.task.UpdateTask
import com.escodro.task.mapper.CategoryMapper
import com.escodro.task.mapper.TaskMapper
import com.escodro.task.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class TaskDetailViewModel(
    private val loadTaskUseCase: LoadTask,
    private val updateTaskUseCase: UpdateTask,
    private val loadAllCategories: LoadAllCategories,
    private val taskMapper: TaskMapper,
    private val categoryMapper: CategoryMapper
) : ViewModel() {

    private val _state: MutableStateFlow<TaskDetailState> = MutableStateFlow(TaskDetailState.Error)

    val state: StateFlow<TaskDetailState>
        get() = _state

    private val coroutineDebouncer = CoroutineDebouncer()

    fun setTaskInfo(taskId: Long) = viewModelScope.launch {
        val task = loadTaskUseCase(taskId = taskId)
        val categories = loadAllCategories()
        val viewCategories = categoryMapper.toView(categories)

        if (task != null) {
            val viewTask = taskMapper.toView(task)
            _state.value = TaskDetailState.Loaded(viewTask, viewCategories)
        } else {
            _state.value = TaskDetailState.Error
        }
    }

    fun updateTitle(title: String) {
        coroutineDebouncer(coroutineScope = viewModelScope) {
            _state.value.run {
                if (this is TaskDetailState.Loaded) {
                    val updatedTask = this.task.copy(title = title)
                    updateTask(updatedTask)
                }
            }
        }
    }

    fun updateDescription(description: String) {
        coroutineDebouncer(coroutineScope = viewModelScope) {
            _state.value.run {
                if (this is TaskDetailState.Loaded) {
                    val updatedTask = this.task.copy(description = description)
                    updateTask(updatedTask)
                }
            }
        }
    }

    fun updateCategory(categoryId: Long?) = viewModelScope.launch {
        _state.value.run {
            if (this is TaskDetailState.Loaded) {
                val updatedTask = this.task.copy(categoryId = categoryId)
                updateTask(updatedTask)
            }
        }
    }

    private suspend fun updateTask(task: Task) {
        val mappedTask = taskMapper.toDomain(task)
        updateTaskUseCase(mappedTask)
    }
}
