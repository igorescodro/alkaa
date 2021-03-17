package com.escodro.category.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escodro.categoryapi.mapper.CategoryMapper
import com.escodro.categoryapi.model.Category
import com.escodro.domain.usecase.category.AddCategory
import kotlinx.coroutines.launch

internal class CategoryAddViewModel(
    private val addCategoryUseCase: AddCategory,
    private val categoryMapper: CategoryMapper
) : ViewModel() {

    fun addCategory(name: String, color: Int) {
        if (name.isEmpty()) return

        viewModelScope.launch {
            val category = Category(name = name, color = color)
            val domainCategory = categoryMapper.toDomain(category)
            addCategoryUseCase.invoke(domainCategory)
        }
    }
}
