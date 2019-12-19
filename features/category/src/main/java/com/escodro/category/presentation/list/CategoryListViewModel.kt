package com.escodro.category.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escodro.category.mapper.CategoryMapper
import com.escodro.category.model.Category
import com.escodro.domain.usecase.category.DeleteCategory
import com.escodro.domain.usecase.category.LoadAllCategories
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * [ViewModel] responsible to provide information to [com.escodro.alkaa.databinding
 * .FragmentCategoryBinding].
 */
internal class CategoryListViewModel(
    private val loadCategoriesUseCase: LoadAllCategories,
    private val deleteCategoryUseCase: DeleteCategory,
    private val categoryMapper: CategoryMapper
) : ViewModel() {

    /**
     * Loads all categories.
     */
    fun loadCategories(onListLoaded: (list: List<Category>) -> Unit) =
        viewModelScope.launch {
            loadCategoriesUseCase()
                .map { categoryMapper.toView(it) }
                .collect { onListLoaded(it) }
        }

    /**
     * Deletes the given category.
     *
     * @param category category to be removed
     */
    fun deleteCategory(category: Category) =
        viewModelScope.launch {
            deleteCategoryUseCase(categoryMapper.toDomain(category))
        }
}
