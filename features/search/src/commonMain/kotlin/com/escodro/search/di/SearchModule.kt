package com.escodro.search.di

import com.escodro.di.viewModelDefinition
import com.escodro.navigationapi.provider.NavGraph
import com.escodro.search.mapper.TaskSearchMapper
import com.escodro.search.navigation.SearchNavGraph
import com.escodro.search.presentation.SearchViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Search dependency injection module.
 */
val searchModule = module {

    // Presentation
    viewModelDefinition { SearchViewModel(findTaskUseCase = get(), mapper = get()) }

    // Mappers
    factoryOf(::TaskSearchMapper)

    // Navigation
    factoryOf(::SearchNavGraph) bind NavGraph::class
}
