package com.escodro.category.di

import com.escodro.category.mapper.CategoryMapper
import com.escodro.category.presentation.CategoryDetailViewController
import com.escodro.category.presentation.add.CategoryAddViewModel
import com.escodro.category.presentation.edit.CategoryEditViewModel
import com.escodro.category.presentation.list.CategoryListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Category dependency injection module.
 */
val categoryModule = module {
    viewModel { CategoryListViewModel(get(), get(), get()) }
    viewModel { CategoryEditViewModel(get(), get(), get()) }
    viewModel { CategoryAddViewModel(get(), get()) }

    factory { CategoryMapper() }
    factory { CategoryDetailViewController() }
}
