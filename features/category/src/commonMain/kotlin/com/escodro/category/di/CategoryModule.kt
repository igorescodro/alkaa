package com.escodro.category.di

import com.escodro.category.mapper.CategoryMapper
import com.escodro.category.navigation.CategoryNavGraph
import com.escodro.category.presentation.bottomsheet.CategoryAddViewModel
import com.escodro.category.presentation.bottomsheet.CategoryEditViewModel
import com.escodro.category.presentation.list.CategoryListViewModelImpl
import com.escodro.categoryapi.presentation.CategoryListViewModel
import com.escodro.navigationapi.provider.NavGraph
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Category dependency injection module.
 */
val categoryModule = module {
    viewModelOf(::CategoryListViewModelImpl) bind CategoryListViewModel::class
    viewModelOf(::CategoryAddViewModel)
    viewModelOf(::CategoryEditViewModel)

    // Mapper
    factoryOf(::CategoryMapper)

    // Navigation
    factoryOf(::CategoryNavGraph) bind NavGraph::class
}
