package com.escodro.task.presentation.category

import androidx.lifecycle.ViewModel
import com.escodro.domain.usecase.category.LoadAllCategories
import com.escodro.task.mapper.CategoryMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal class TaskCategoryViewModel(
    private val loadAllCategories: LoadAllCategories,
    private val categoryMapper: CategoryMapper
) : ViewModel() {

    fun loadCategories(): Flow<CategoryState> = flow {
        val categoryList = loadAllCategories()

        if (categoryList.isNotEmpty()) {
            val mappedList = categoryMapper.toView(categoryList)
            emit(CategoryState.Loaded(mappedList))
        } else {
            emit(CategoryState.Empty)
        }
    }
}
