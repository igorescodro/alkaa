package com.escodro.alkaa.di

import com.escodro.alkaa.mapper.CategoryMapper
import org.koin.dsl.module

/**
 * Application module.
 */
val appModule = module {
    factory { CategoryMapper() }
}
