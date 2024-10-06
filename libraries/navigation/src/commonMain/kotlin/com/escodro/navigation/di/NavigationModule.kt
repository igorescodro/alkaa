package com.escodro.navigation.di

import com.escodro.navigation.controller.NavEventController
import com.escodro.navigation.provider.NavGraphProvider
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val navigationModule = module {

    singleOf(::NavEventController)
    single { NavGraphProvider(get(), getAll()) }
}
