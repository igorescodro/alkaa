package com.escodro.category.presentation.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escodro.category.mapper.CategoryMapper
import com.escodro.category.model.Category
import com.escodro.domain.usecase.category.InsertCategory
import kotlinx.coroutines.launch

internal class CategoryAddViewModel(
    private val insertCategory: InsertCategory,
    private val categoryMapper: CategoryMapper
) : ViewModel() {

    private val _categoryViewState = MutableLiveData<CategoryAddUIState>()

    /**
     * The current category color.
     */
    var currentColor: String? = null

    /**
     * The screen view state to be observed.
     */
    val uiState: LiveData<CategoryAddUIState>
        get() = _categoryViewState

    /**
     * Saves the category.
     *
     * @param name the category name
     * @param color the category color
     */
    fun saveCategory(name: String, color: String) {
        if (name.isBlank()) {
            _categoryViewState.value = CategoryAddUIState.EmptyName
            return
        }

        val category = Category(name = name, color = color)

        viewModelScope.launch {
            val domainCategory = categoryMapper.toDomain(category)
            insertCategory(domainCategory)
            _categoryViewState.value = CategoryAddUIState.Saved
        }
    }
}
