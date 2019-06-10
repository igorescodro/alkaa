package com.escodro.domain.di

import com.escodro.domain.mapper.CategoryMapper
import com.escodro.domain.usecase.DeleteCategory
import com.escodro.domain.usecase.LoadAllCategories
import com.escodro.domain.usecase.LoadCategory
import com.escodro.domain.usecase.SaveCategory
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
