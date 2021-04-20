package com.escodro.category.presentation.bottomsheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escodro.category.mapper.CategoryMapper
import com.escodro.categoryapi.model.Category
import com.escodro.domain.usecase.category.DeleteCategory
import com.escodro.domain.usecase.category.UpdateCategory
import kotlinx.coroutines.launch

internal class CategoryEditViewModel(
    private val updateCategoryUseCase: UpdateCategory,
    private val deleteCategoryUseCase: DeleteCategory,
    private val mapper: CategoryMapper
) : ViewModel() {

    fun updateCategory(category: Category) {
        if (category.name.isEmpty()) return

        viewModelScope.launch {
            val domainCategory = mapper.toDomain(category)
            updateCategoryUseCase(domainCategory)
        }
    }

    fun deleteCategory(category: Category) = viewModelScope.launch {
        val domainCategory = mapper.toDomain(category)
        deleteCategoryUseCase(domainCategory)
    }
}
