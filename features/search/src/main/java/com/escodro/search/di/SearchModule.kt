package com.escodro.search.di

import com.escodro.search.mapper.TaskSearchMapper
import com.escodro.search.presentation.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * Search dependency injection module.
 */
val searchModule = module {

    // Presentation
    viewModelOf(::SearchViewModel)

    // Mappers
    factoryOf(::TaskSearchMapper)
}
