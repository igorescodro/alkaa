package com.escodro.category.di

import com.escodro.category.mapper.CategoryMapper
import com.escodro.category.presentation.bottomsheet.CategoryAddViewModel
import com.escodro.category.presentation.bottomsheet.CategoryEditViewModel
import com.escodro.category.presentation.list.CategoryListViewModelImpl
import com.escodro.categoryapi.presentation.CategoryListViewModel
import com.escodro.di.viewModelDefinition
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * Category dependency injection module.
 */
val categoryModule = module {
    viewModelDefinition<CategoryListViewModel> {
        CategoryListViewModelImpl(
            loadAllCategories = get(),
            categoryMapper = get(),
        )
    }
    viewModelDefinition {
        CategoryAddViewModel(
            addCategoryUseCase = get(),
            applicationScope = get(),
            categoryMapper = get(),
        )
    }
    viewModelDefinition {
        CategoryEditViewModel(
            loadCategoryUseCase = get(),
            updateCategoryUseCase = get(),
            deleteCategoryUseCase = get(),
            applicationScope = get(),
            mapper = get(),
        )
    }

    // Mapper
    factoryOf(::CategoryMapper)
}
