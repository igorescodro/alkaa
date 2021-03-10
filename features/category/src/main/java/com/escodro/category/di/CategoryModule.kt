package com.escodro.category.di

import com.escodro.category.mapper.CategoryMapperImpl
import com.escodro.category.presentation.CategoryListViewModelImpl
import com.escodro.categoryapi.mapper.CategoryMapper
import com.escodro.categoryapi.presentation.CategoryListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Category dependency injection module.
 */
val categoryModule = module {
    viewModel<CategoryListViewModel> { CategoryListViewModelImpl(get(), get()) }
    factory<CategoryMapper> { CategoryMapperImpl() }
}
