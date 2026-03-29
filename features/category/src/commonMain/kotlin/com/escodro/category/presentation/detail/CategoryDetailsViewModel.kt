package com.escodro.category.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escodro.domain.usecase.category.LoadCategory
import com.escodro.domain.usecase.task.AddTask
import com.escodro.domain.usecase.task.UpdateTaskStatus
import com.escodro.domain.usecase.taskwithcategory.LoadCategoryTasks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

internal class CategoryDetailsViewModel(
    private val loadCategory: LoadCategory,
    private val loadCategoryTasks: LoadCategoryTasks,
    private val addTask: AddTask,
    private val updateTaskStatus: UpdateTaskStatus,
    private val mapper: CategoryDetailsMapper,
) : ViewModel() {

    fun loadContent(categoryId: Long): Flow<CategoryDetailsState> = combine(
        flow { emit(loadCategory(categoryId)) },
        loadCategoryTasks(categoryId),
    ) { category, groups ->
        if (category == null) {
            CategoryDetailsState.Error(IllegalStateException("Category not found"))
        } else {
            mapper.toViewState(category, groups)
        }
    }.catch { e ->
        emit(CategoryDetailsState.Error(e))
    }

    fun addTask(title: String, dueDate: LocalDateTime?, categoryId: Long) {
        if (title.isBlank()) return
        viewModelScope.launch {
            addTask(mapper.toTask(title, dueDate, categoryId))
        }
    }

    fun updateTaskStatus(taskId: Long) {
        viewModelScope.launch {
            updateTaskStatus.invoke(taskId)
        }
    }
}
