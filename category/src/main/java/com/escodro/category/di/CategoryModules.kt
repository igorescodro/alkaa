package com.escodro.category.di

import com.escodro.category.list.CategoryListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val categoryModule = module {
    // Category List
    viewModel { CategoryListViewModel(get(), get()) }
}
