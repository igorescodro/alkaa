package com.escodro.task.presentation.detail.category

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escodro.domain.usecase.category.LoadAllCategories
import com.escodro.domain.usecase.task.UpdateTaskCategory
import com.escodro.task.mapper.CategoryMapper
import com.escodro.task.presentation.detail.main.CategoryId
import com.escodro.task.presentation.detail.main.TaskId
import kotlinx.coroutines.launch

internal class TaskCategoryViewModel(
    private val loadAllCategories: LoadAllCategories,
    private val updateTaskCategory: UpdateTaskCategory,
    private val categoryMapper: CategoryMapper
) : ViewModel() {

    private val _state: MutableState<TaskCategoryState> = mutableStateOf(TaskCategoryState.Empty)

    val state: State<TaskCategoryState>
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

    fun updateCategory(taskId: TaskId, categoryId: CategoryId) = viewModelScope.launch {
        _state.value.run {
            if (this is TaskCategoryState.Loaded) {
                updateTaskCategory(taskId = taskId.value, categoryId = categoryId.value)
            }
        }
    }
}
