package com.escodro.domain.di

import com.escodro.domain.usecase.DeleteCategory
import com.escodro.domain.usecase.LoadCategories
import org.koin.dsl.module

/**
 * Domain dependency injection module.
 */
val domainModule = module {

    single { LoadCategories(get(), get()) }
    single { DeleteCategory(get(), get()) }
}
