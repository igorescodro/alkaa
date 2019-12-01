package com.escodro.category.di

import com.escodro.category.mapper.CategoryMapper
import com.escodro.category.presentation.detail.CategoryDetailViewModel
import com.escodro.category.presentation.list.CategoryListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val categoryModule = module {
    viewModel { CategoryListViewModel(get(), get(), get()) }
    viewModel { CategoryDetailViewModel(get(), get(), get()) }

    factory { CategoryMapper() }
}
