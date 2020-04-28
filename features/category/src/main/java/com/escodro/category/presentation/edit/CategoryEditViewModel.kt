package com.escodro.category.presentation.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escodro.category.mapper.CategoryMapper
import com.escodro.category.model.Category
import com.escodro.domain.usecase.category.LoadCategory
import com.escodro.domain.usecase.category.UpdateCategory
import kotlinx.coroutines.launch

internal class CategoryEditViewModel(
    private val loadCategoryUseCase: LoadCategory,
    private val updateCategory: UpdateCategory,
    private val categoryMapper: CategoryMapper
) : ViewModel() {

    private val _categoryViewState = MutableLiveData<CategoryEditUIState>()

    private var currentCategory: Category? = null

    /**
     * The screen view state to be observed.
     */
    val uiState: LiveData<CategoryEditUIState>
        get() = _categoryViewState

    /**
     * The current category color.
     */
    var currentColor: String? = null

    /**
     * Loads the category based on the given id.
     *
     * @param categoryId the id to be loaded
     */
    fun loadCategory(categoryId: Long) = viewModelScope.launch {
        val category = loadCategoryUseCase(categoryId)

        if (category != null) {
            val viewCategory = categoryMapper.toView(category)
            currentCategory = viewCategory
            _categoryViewState.value = CategoryEditUIState.Loaded(viewCategory)
            currentColor = viewCategory.color
        } else {
            _categoryViewState.value = CategoryEditUIState.Error
        }
    }

    /**
     * Saves the category.
     *
     * @param name the category name
     * @param color the category color
     */
    fun saveCategory(name: String, color: String) {
        if (name.isBlank()) {
            _categoryViewState.value = CategoryEditUIState.EmptyName
            return
        }

        val updatedCategory = currentCategory?.copy(name = name, color = color)
        if (updatedCategory == null) {
            _categoryViewState.value = CategoryEditUIState.Error
            return
        }

        currentCategory = updatedCategory

        viewModelScope.launch {
            val domainCategory = categoryMapper.toDomain(updatedCategory)
            updateCategory(domainCategory)
            _categoryViewState.value = CategoryEditUIState.Saved
        }
    }
}
