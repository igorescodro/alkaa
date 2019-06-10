package com.escodro.domain.di

import com.escodro.domain.mapper.CategoryMapper
import com.escodro.domain.usecase.category.DeleteCategory
import com.escodro.domain.usecase.category.LoadAllCategories
import com.escodro.domain.usecase.category.LoadCategory
import com.escodro.domain.usecase.category.SaveCategory
import org.koin.dsl.module

/**
 * Domain dependency injection module.
 */
val domainModule = module {

    single { CategoryMapper() }

    single { LoadAllCategories(get(), get()) }
    single { LoadCategory(get(), get()) }
    single { SaveCategory(get(), get()) }
    single { DeleteCategory(get(), get()) }
}
