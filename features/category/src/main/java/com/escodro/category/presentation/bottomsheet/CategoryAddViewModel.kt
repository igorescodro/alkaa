package com.escodro.category.presentation.bottomsheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escodro.category.mapper.CategoryMapper
import com.escodro.categoryapi.model.Category
import com.escodro.domain.usecase.category.AddCategory
import kotlinx.coroutines.launch

internal class CategoryAddViewModel(
    private val addCategoryUseCase: AddCategory,
    private val categoryMapper: CategoryMapper
) : ViewModel() {

    fun addCategory(category: Category) {
        if (category.name.isEmpty()) return

        viewModelScope.launch {
            val domainCategory = categoryMapper.toDomain(category)
            addCategoryUseCase.invoke(domainCategory)
        }
    }
}
