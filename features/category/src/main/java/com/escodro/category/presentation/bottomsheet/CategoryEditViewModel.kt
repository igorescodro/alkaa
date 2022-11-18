package com.escodro.category.presentation.bottomsheet

import androidx.lifecycle.ViewModel
import com.escodro.category.mapper.CategoryMapper
import com.escodro.categoryapi.model.Category
import com.escodro.domain.usecase.category.DeleteCategory
import com.escodro.domain.usecase.category.LoadCategory
import com.escodro.domain.usecase.category.UpdateCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

internal class CategoryEditViewModel(
    private val loadCategoryUseCase: LoadCategory,
    private val updateCategoryUseCase: UpdateCategory,
    private val deleteCategoryUseCase: DeleteCategory,
    private val applicationScope: CoroutineScope,
    private val mapper: CategoryMapper
) : ViewModel() {

    fun loadCategory(categoryId: Long) = flow {
        val category = loadCategoryUseCase(categoryId)
        if (category != null) {
            val viewCategory = mapper.toView(category)
            emit(CategorySheetState.Loaded(viewCategory))
        } else {
            emit(CategorySheetState.Empty)
        }
    }

    fun updateCategory(category: Category) {
        if (category.name.isEmpty()) return

        applicationScope.launch {
            val domainCategory = mapper.toDomain(category)
            updateCategoryUseCase(domainCategory)
        }
    }

    fun deleteCategory(category: Category) = applicationScope.launch {
        val domainCategory = mapper.toDomain(category)
        deleteCategoryUseCase(domainCategory)
    }
}
