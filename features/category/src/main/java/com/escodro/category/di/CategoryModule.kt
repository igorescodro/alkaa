package com.escodro.category.di

import com.escodro.category.mapper.CategoryMapper
import com.escodro.category.presentation.bottomsheet.CategoryAddViewModel
import com.escodro.category.presentation.bottomsheet.CategoryEditViewModel
import com.escodro.category.presentation.list.CategoryListViewModelImpl
import com.escodro.categoryapi.presentation.CategoryListViewModel
import com.escodro.core.coroutines.ApplicationScope
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Category dependency injection module.
 */
val categoryModule = module {
    viewModelOf(::CategoryListViewModelImpl) bind CategoryListViewModel::class
    viewModelOf(::CategoryAddViewModel)
    viewModel {
        CategoryEditViewModel(
            loadCategoryUseCase = get(),
            updateCategoryUseCase = get(),
            deleteCategoryUseCase = get(),
            applicationScope = get(ApplicationScope),
            mapper = get()
        )
    }

    // Mapper
    factoryOf(::CategoryMapper)
}
