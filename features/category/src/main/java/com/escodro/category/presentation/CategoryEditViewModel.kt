package com.escodro.category.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escodro.categoryapi.mapper.CategoryMapper
import com.escodro.categoryapi.model.Category
import com.escodro.domain.usecase.category.UpdateCategory
import kotlinx.coroutines.launch

internal class CategoryEditViewModel(
    private val updateCategoryUseCase: UpdateCategory,
    private val mapper: CategoryMapper
) : ViewModel() {

    fun updateCategory(category: Category) {
        if (category.name.isEmpty()) return

        viewModelScope.launch {
            val domainCategory = mapper.toDomain(category)
            updateCategoryUseCase(domainCategory)
        }
    }
}