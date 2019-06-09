package com.escodro.domain.di

import com.escodro.domain.mapper.CategoryMapper
import com.escodro.domain.usecase.DeleteCategory
import com.escodro.domain.usecase.LoadAllCategories
import org.koin.dsl.module

/**
 * Domain dependency injection module.
 */
val domainModule = module {

    single { CategoryMapper() }
    single { LoadAllCategories(get(), get()) }
    single { DeleteCategory(get(), get()) }
}
