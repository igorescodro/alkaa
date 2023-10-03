package com.escodro.shared.di

import com.escodro.di.viewModelDefinition
import com.escodro.shared.AppViewModel
import com.escodro.shared.mapper.AppThemeOptionsMapper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val sharedModule = module {
    singleOf(::AppThemeOptionsMapper)
    viewModelDefinition { AppViewModel(get(), get()) }
}
