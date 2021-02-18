package com.escodro.task.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escodro.domain.usecase.category.LoadAllCategories
import com.escodro.domain.usecase.task.UpdateTaskCategory
import com.escodro.task.mapper.CategoryMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class TaskCategoryViewModel(
    private val loadAllCategories: LoadAllCategories,
    private val updateTaskCategory: UpdateTaskCategory,
    private val categoryMapper: CategoryMapper
) : ViewModel() {

    private val _state: MutableStateFlow<TaskCategoryState> =
        MutableStateFlow(TaskCategoryState.Empty)

    val state: StateFlow<TaskCategoryState>
        get() = _state

    fun loadCategories() = viewModelScope.launch {
        val categoryList = loadAllCategories()

        if (categoryList.isNotEmpty()) {
            val mappedList = categoryMapper.toView(categoryList)
            _state.value = TaskCategoryState.Loaded(mappedList)
        } else {
            _state.value = TaskCategoryState.Empty
        }
    }

    fun updateCategory(taskId: Long, categoryId: Long?) = viewModelScope.launch {
        _state.value.run {
            if (this is TaskCategoryState.Loaded) {
                updateTaskCategory(taskId = taskId, categoryId = categoryId)
            }
        }
    }
}
