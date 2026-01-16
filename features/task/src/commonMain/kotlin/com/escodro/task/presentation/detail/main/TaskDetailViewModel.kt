package com.escodro.task.presentation.detail.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escodro.coroutines.AppCoroutineScope
import com.escodro.coroutines.CoroutineDebouncer
import com.escodro.domain.usecase.checklist.AddChecklistItem
import com.escodro.domain.usecase.checklist.DeleteChecklistItem
import com.escodro.domain.usecase.checklist.LoadChecklistItems
import com.escodro.domain.usecase.checklist.UpdateChecklistItem
import com.escodro.domain.usecase.task.LoadTask
import com.escodro.task.mapper.ChecklistItemMapper
import com.escodro.task.model.ChecklistItem
import com.escodro.domain.usecase.task.UpdateTaskCategory
import com.escodro.domain.usecase.task.UpdateTaskDescription
import com.escodro.domain.usecase.task.UpdateTaskTitle
import com.escodro.task.mapper.TaskMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@Suppress("LongParameterList")
internal class TaskDetailViewModel(
    private val loadTaskUseCase: LoadTask,
    private val updateTaskTitle: UpdateTaskTitle,
    private val updateTaskDescription: UpdateTaskDescription,
    private val updateTaskCategory: UpdateTaskCategory,
    private val loadChecklistItemsUseCase: LoadChecklistItems,
    private val addChecklistItemUseCase: AddChecklistItem,
    private val updateChecklistItemUseCase: UpdateChecklistItem,
    private val deleteChecklistItemUseCase: DeleteChecklistItem,
    private val coroutineDebouncer: CoroutineDebouncer,
    private val applicationScope: AppCoroutineScope,
    private val taskMapper: TaskMapper,
    private val checklistItemMapper: ChecklistItemMapper,
) : ViewModel() {

    fun loadTaskInfo(taskId: TaskId): Flow<TaskDetailState> = flow {
        val task = loadTaskUseCase(taskId = taskId.value)

        if (task != null) {
            val viewTask = taskMapper.toView(task)
            loadChecklistItemsUseCase(taskId.value).collect { checklist ->
                val viewChecklist = checklist.map { checklistItemMapper.toView(it) }
                emit(TaskDetailState.Loaded(viewTask, viewChecklist))
            }
        } else {
            emit(TaskDetailState.Error)
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

    fun addChecklistItem(taskId: TaskId, title: String) {
        applicationScope.launch {
            val item = ChecklistItem(taskId = taskId.value, title = title)
            addChecklistItemUseCase(checklistItemMapper.toDomain(item))
        }
    }

    fun updateChecklistItem(item: ChecklistItem) {
        applicationScope.launch {
            updateChecklistItemUseCase(checklistItemMapper.toDomain(item))
        }
    }

    fun deleteChecklistItem(item: ChecklistItem) {
        applicationScope.launch {
            deleteChecklistItemUseCase(checklistItemMapper.toDomain(item))
        }
    }
}
