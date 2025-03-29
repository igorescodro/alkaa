package com.escodro.category.presentation.bottomsheet

import androidx.lifecycle.ViewModel
import com.escodro.category.mapper.CategoryMapper
import com.escodro.categoryapi.model.Category
import com.escodro.coroutines.AppCoroutineScope
import com.escodro.domain.usecase.category.AddCategory

internal class CategoryAddViewModel(
    private val addCategoryUseCase: AddCategory,
    private val applicationScope: AppCoroutineScope,
    private val categoryMapper: CategoryMapper,
) : ViewModel() {

    fun addCategory(category: Category) {
        if (category.name.isEmpty()) return

        applicationScope.launch {
            val domainCategory = categoryMapper.toDomain(category)
            addCategoryUseCase.invoke(domainCategory)
        }
    }
}
