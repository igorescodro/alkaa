package com.escodro.alkaa.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escodro.alkaa.mapper.CategoryMapper
import com.escodro.alkaa.model.Category
import com.escodro.domain.usecase.category.LoadAllCategories
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * [ViewModel] responsible to provide information to [MainActivity].
 */
internal class MainViewModel(
    private val loadAllCategories: LoadAllCategories,
    private val categoryMapper: CategoryMapper
) : ViewModel() {

    /**
     * Loads all categories.
     */
    fun loadCategories(onListLoaded: (list: List<Category>) -> Unit) = viewModelScope.launch {
        loadAllCategories()
            .map { categoryMapper.toView(it) }
            .collect { onListLoaded(it) }
    }
}
