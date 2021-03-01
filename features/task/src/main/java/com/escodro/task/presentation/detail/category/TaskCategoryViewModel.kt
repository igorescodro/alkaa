package com.escodro.task.presentation.detail.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escodro.domain.usecase.category.LoadAllCategories
import com.escodro.domain.usecase.task.UpdateTaskCategory
import com.escodro.task.mapper.CategoryMapper
import com.escodro.task.presentation.detail.main.CategoryId
import com.escodro.task.presentation.detail.main.TaskId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

internal class TaskCategoryViewModel(
    private val loadAllCategories: LoadAllCategories,
    private val updateTaskCategory: UpdateTaskCategory,
    private val categoryMapper: CategoryMapper
) : ViewModel() {

    fun loadCategories(): Flow<TaskCategoryState> = flow {
        val categoryList = loadAllCategories()

        if (categoryList.isNotEmpty()) {
            val mappedList = categoryMapper.toView(categoryList)
            emit(TaskCategoryState.Loaded(mappedList))
        } else {
            emit(TaskCategoryState.Empty)
        }
    }

    fun updateCategory(taskId: TaskId, categoryId: CategoryId) = viewModelScope.launch {
        updateTaskCategory(taskId = taskId.value, categoryId = categoryId.value)
    }
}
