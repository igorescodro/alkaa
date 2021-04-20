package com.escodro.category.presentation.list

import com.escodro.category.mapper.CategoryMapper
import com.escodro.categoryapi.presentation.CategoryListViewModel
import com.escodro.categoryapi.presentation.CategoryState
import com.escodro.domain.usecase.category.LoadAllCategories
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

internal class CategoryListViewModelImpl(
    private val loadAllCategories: LoadAllCategories,
    private val categoryMapper: CategoryMapper
) : CategoryListViewModel() {

    override fun loadCategories(): Flow<CategoryState> = flow {
        loadAllCategories().collect { categoryList ->
            if (categoryList.isNotEmpty()) {
                val mappedList = categoryMapper.toView(categoryList)
                emit(CategoryState.Loaded(mappedList))
            } else {
                emit(CategoryState.Empty)
            }
        }
    }
}
