package com.escodro.category.di

import com.escodro.category.mapper.CategoryMapper
import com.escodro.category.presentation.bottomsheet.CategoryAddViewModel
import com.escodro.category.presentation.bottomsheet.CategoryEditViewModel
import com.escodro.category.presentation.list.CategoryListViewModelImpl
import com.escodro.categoryapi.presentation.CategoryListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Category dependency injection module.
 */
val categoryModule = module {
    viewModel<CategoryListViewModel> { CategoryListViewModelImpl(get(), get()) }
    viewModel { CategoryAddViewModel(get(), get()) }
    viewModel { CategoryEditViewModel(get(), get(), get()) }

    // Mapper
    factory { CategoryMapper() }
}
